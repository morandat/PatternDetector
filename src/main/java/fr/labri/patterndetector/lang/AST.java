package fr.labri.patterndetector.lang;

import xtc.util.Pair;

import java.util.*;
import java.util.concurrent.TimeUnit;

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

    public static Selector newSelector(Selector s, Pair<Selector> ss) {
        if (ss.isEmpty())
            return s;
        return new CompositeSelector(s, ss.head());
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

    public static class Pattern {
        List<Transition> _transitions = new ArrayList<>();

        public void addAlias(String name) {
        }

        public void addTransitions(Pair<Transition> transitions) {
            transitions.addTo(_transitions);
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
    }

    public static class Expression {
    }

    public static class Selector extends Expression {
    }

    public static class SimpleSelector extends Selector {
        public final String _reference;

        SimpleSelector(String reference) {
            _reference = reference;
        }

        @Override
        public String toString() {
            return _reference;
        }
    }

    public static class CompositeSelector extends Selector {
        public final Selector _reference;
        public final Selector _field;

        CompositeSelector(Selector reference, Selector field) {
            _reference = reference;
            _field = field;
        }

        @Override
        public String toString() {
            return _reference + "." + _field;
        }
    }

    public class RangeSpec {}

    public static class FunctionCall extends Expression {
        public final String _name;
        public final List<Expression> _args = new ArrayList<>();

        FunctionCall(String name, Expression... args) {
            _name = name;
            _args.addAll(Arrays.asList(args));
        }

        @Override
        public String toString() {
            return String.format("%s(%s)", _name, String.join(", ", new Iterable<CharSequence>() {
                @Override
                public Iterator<CharSequence> iterator() {
                    final Iterator<Expression> it = _args.iterator();
                    return new Iterator<CharSequence>() {
                        @Override
                        public boolean hasNext() {
                            return it.hasNext();
                        }

                        @Override
                        public CharSequence next() {
                            Expression n = it.next();
                            return n == null ? null : n.toString();
                        }
                    };
                }
            }));
        }
    }

    public static class Literal<E> extends Expression {
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
    }

    public static class StringLiteral extends Literal<String> {
        StringLiteral(String value) {
            super(value);
        }

        @Override
        public int hashCode() {
            return _value.hashCode();
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
    }

    public static class NotYetImplementedException extends RuntimeException {

    }
}
