package fr.labri.patterndetector.lang;

import xtc.util.Pair;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by morandat on 10/05/2016.
 */
public class AST {

    public static Rule newRule(String name) {
        return new Rule(name);
    }

    public static Transition newTransition() {
        return null;
    }

    public static Transition newNak() {
        return null;
    }

    public static Pattern newSimplePattern(String name) {
        return new SymbolPattern(name);
    }

    public static Pattern newCompositePattern(Pair<Pattern> patterns) {
        return new CompositePattern(patterns);
    }

    public static Pattern newKleene(Pattern pattern) {
        return new KleenePattern(pattern);
    }

    public static NumberLiteral newNumber(String value, int base) {
        return new NumberLiteral(Long.parseLong(value, base));
    }

    public static StringLiteral newString(String value) {
        return new StringLiteral(value);
    }

    public static TimeValue newTimeValue(String value, TimeUnit unit) {
        long v = Long.parseLong(value);
        switch (unit) {
            case NANOSECONDS:
                break;
            case MICROSECONDS:
                break;
            case MILLISECONDS:
                break;
            case SECONDS:
                break;
            case MINUTES:
                v *= 60;
                break;
            case HOURS:
                v *= 60 * 60;
                break;
            case DAYS:
                v *= 60 * 60 * 24;
                break;
        }
        return new TimeValue(v);
    }

    public static Selector newSelector(String s) {
        return new SimpleSelector(s);
    }

    public static Selector newSequenceSelector(String s, Pair<Expression> exprs) {
        if (exprs.tail().isEmpty())
            return new IndexSelector(new SimpleSelector(s), exprs.head());
        return new RangeSelector(new SimpleSelector(s), exprs.head(), exprs.get(1));
    }

    public static Selector newSelector(Selector s, Pair<Selector> ss) {
        if (ss.isEmpty())
            return s;
        Selector head = ss.head();
        if (head instanceof  FieldSelector) {
            return new FieldSelector(s, ((FieldSelector)head)._field);
        } else if (head instanceof SimpleSelector) {
            return new FieldSelector(s, ((SimpleSelector)head)._name);
        }
        throw new NotYetImplementedException();
    }

    public static Pattern newReferencePattern(String name) {
        throw new NotYetImplementedException();
    }

    public static Expression newFunctionCall(String name, Expression... args) {
        return new FunctionCall(name, args);
    }

    public static class Rule {
        public final String _name;
        List<Pattern> _patterns = new ArrayList<>();
        List<Expression> _constraints = new ArrayList<>();

        Rule(String name) {
            _name = name;
        }

        public Rule appendConstraint(Expression constraint) {
            _constraints.add(constraint);
            return this;
        }

        public Rule appendPattern(Pattern pattern) {
            _patterns.add(pattern);
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Rule)) return false;

            Rule rule = (Rule) o;

            if (_name != null ? !_name.equals(rule._name) : rule._name != null) return false;
            if (_patterns != null ? !_patterns.equals(rule._patterns) : rule._patterns != null) return false;
            return _constraints != null ? _constraints.equals(rule._constraints) : rule._constraints == null;
        }

        @Override
        public int hashCode() {
            int result = _name != null ? _name.hashCode() : 0;
            result = 31 * result + (_patterns != null ? _patterns.hashCode() : 0);
            result = 31 * result + (_constraints != null ? _constraints.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return String.format("%s:\n\t%s\n\t%s", _name, _patterns, _constraints);
        }
    }

    public static abstract class Pattern {
        final List<Transition> _transitions = new ArrayList<>();
        final List<String> _names = new ArrayList<>();

        public void addAlias(String name) {
            _names.add(name);
        }

        public void addTransitions(Pair<Transition> transitions) {
            transitions.addTo(_transitions);
        }

        abstract public void accept(PatternVisitor visitor);

        public boolean hasName(String name) {
            return _names.contains(name);
        }
    }

    public static class SymbolPattern extends Pattern {
        public final String _symbol;

        SymbolPattern(String name) {
            _symbol = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SymbolPattern)) return false;

            SymbolPattern that = (SymbolPattern) o;

            return _symbol != null ? _symbol.equals(that._symbol) : that._symbol == null;

        }

        @Override
        public int hashCode() {
            return _symbol != null ? _symbol.hashCode() : 0;
        }

        @Override
        public String toString() {
            return _symbol;
        }

        @Override
        public void accept(PatternVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class CompositePattern extends Pattern {
        public final List<Pattern> _patterns = new ArrayList<>();

        CompositePattern(Pair<Pattern> patterns) {
            patterns.addTo(_patterns);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CompositePattern)) return false;

            CompositePattern that = (CompositePattern) o;

            return _patterns != null ? _patterns.equals(that._patterns) : that._patterns == null;

        }

        @Override
        public int hashCode() {
            return _patterns != null ? _patterns.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "(" + _patterns + ")";
        }

        @Override
        public void accept(PatternVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class KleenePattern extends Pattern {
        public final Pattern _pattern;

        KleenePattern(Pattern pattern) {
            _pattern = pattern;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof KleenePattern)) return false;

            KleenePattern that = (KleenePattern) o;

            return _pattern != null ? _pattern.equals(that._pattern) : that._pattern == null;

        }

        @Override
        public int hashCode() {
            return _pattern != null ? _pattern.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "(" + _pattern.toString() + ")*";
        }

        @Override
        public void accept(PatternVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static abstract class Expression {
        public abstract void accept(ExpressionVisitor visitor);
    }

    public static abstract class Selector extends Expression {
    }

    public static class SimpleSelector extends Selector {
        public final String _name;
        public Pattern _reference;

        SimpleSelector(String reference) {
            _name = reference;
        }

        @Override
        public String toString() {
            return _name;
        }

        @Override
        public void accept(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class FieldSelector extends Selector {
        public final Selector _reference;
        public final String _field;

        FieldSelector(Selector reference, String field) {
            _reference = reference;
            _field = field;
        }

        @Override
        public String toString() {
            return _reference + "." + _field;
        }

        @Override
        public void accept(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    static public abstract class SequenceSelector extends Selector {
        final Selector _selector;

        public SequenceSelector(Selector selector) {
            _selector = selector;
        }
    }

    static public class IndexSelector extends SequenceSelector {
        final Expression _index;

        public IndexSelector(Selector selector, Expression index) {
            super(selector);
            _index = index;
        }

        @Override
        public void accept(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    static public class RangeSelector extends SequenceSelector {
        final Expression _left, _right;

        public RangeSelector(Selector selector, Expression left, Expression right) {
            super(selector);
            _left = left;
            _right = right;
        }

        @Override
        public void accept(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class FunctionCall extends Expression {
        public final String _name;
        public final List<Expression> _args = new ArrayList<>();

        FunctionCall(String name, Expression... args) {
            _name = name;
            _args.addAll(Arrays.asList(args));
        }

        @Override
        public String toString() {
            return String.format("%s(%s)",
                    _name,
                    String.join(", ", Stream.of(_args).map((x) -> x.toString()).collect(Collectors.toList())));
        }

        @Override
        public void accept(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static abstract class Literal<E> extends Expression {
        public final E _value;

        Literal(E value) {
            _value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(getClass().isInstance(o))) return false;

            return _value.equals(((Literal)o)._value);
        }

    }

    public static class NumberLiteral extends Literal<Long> {
        NumberLiteral(long value) {
            super(value);
        }

        @Override
        public int hashCode() {
            return (int) (_value % Integer.MAX_VALUE);
        }

        @Override
        public void accept(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class StringLiteral extends Literal<String> {
        StringLiteral(String value) {
            super(value);
        }

        @Override
        public int hashCode() {
            return _value.hashCode();
        }

        @Override
        public void accept(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class Transition {
    }

    public static class TimeValue extends Literal<Long> {

        TimeValue(long value) {
            super(value);
        }

        @Override
        public int hashCode() {
            return (int) (_value % Integer.MAX_VALUE);
        }
        @Override
        public String toString(){
            return _value + "s";
        }

        @Override
        public void accept(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class NotYetImplementedException extends RuntimeException {
    }

    static class PatternVisitor {
        void visit(Rule r) {
            for (Pattern p: r._patterns) {
                p.accept(this);
            }
        }
        void visit(Pattern pattern) {}

        void visit(SymbolPattern symbol) { visit((Pattern) symbol); }
        void visit(CompositePattern composite) { visit((Pattern) composite); }
        void visit(KleenePattern kleene) { visit((Pattern) kleene); }
    }

    static class ExpressionVisitor {
        public void visit(Rule rule) {
            for (Expression expr: rule._constraints) {
                expr.accept(this);
            }
        }

        void visit(Expression expr) {}

        void visit(Selector selector) { visit((Expression) selector); }
        void visit(SimpleSelector selector) { visit((Selector) selector); }
        void visit(FieldSelector selector) { visit((Selector) selector); }
        void visit(SequenceSelector selector) { visit((Selector) selector); }
        void visit(IndexSelector selector) { visit((SequenceSelector) selector); }
        void visit(RangeSelector selector) { visit((SequenceSelector) selector); }

        void visit(FunctionCall call) { visit((Expression) call); }

        void visit(Literal literal) { visit((Expression) literal); }
        void visit(NumberLiteral number) { visit((Literal) number); }
        void visit(TimeValue time) { visit((Literal) time); }
        void visit(StringLiteral string) { visit((Literal) string); }
    }
}
