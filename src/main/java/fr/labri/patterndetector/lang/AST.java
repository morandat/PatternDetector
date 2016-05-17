package fr.labri.patterndetector.lang;

import xtc.util.Pair;

import java.util.ArrayList;
import java.util.List;
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

    public static TimeValue newTimeValue(String value, TimeUnit unit) {
        int v = Integer.parseInt(value);
        return new TimeValue(v, unit);
    }

    public static Pattern newReferencePattern(String name) {
        throw new NotYetImplementedException();
    }

    public static class Rule {
        public final String _name;
        List<Pattern> _patterns = new ArrayList<>();
        List<Constraint> _constraints = new ArrayList<>();

        Rule(String name) {
            _name = name;
        }

        public Rule appendConstraint(Constraint constraint) {
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
            return String.format("%s:\n\t%s\n\t", _name, _patterns, _constraints);
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

    public static class Constraint {
    }

    public static class Transition {
    }

    public static class TimeValue {
        public final int _value;
        public final TimeUnit _unit;

        TimeValue(int value, TimeUnit unit) {
            _value = value;
            _unit = unit;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TimeValue)) return false;

            TimeValue timeValue = (TimeValue) o;

            if (_value != timeValue._value) return false;
            return _unit == timeValue._unit;

        }

        @Override
        public int hashCode() {
            int result = _value;
            result = 31 * result + (_unit != null ? _unit.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return _value + " " + _unit;
        }
    }

    public static class NotYetImplementedException extends RuntimeException {

    }
}
