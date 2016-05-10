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

        public void appendConstraint(Constraint constraint) {
            _constraints.add(constraint);
        }

        public void appendPattern(Pattern pattern) {
            _patterns.add(pattern);
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
    }

    public static class CompositePattern extends Pattern {
        public final List<Pattern> _patterns = new ArrayList<>();

        CompositePattern(Pair<Pattern> patterns) {
            patterns.addTo(_patterns);
        }
    }

    public static class KleenePattern extends Pattern {
        public final Pattern _pattern;
        KleenePattern(Pattern pattern) {
            _pattern = pattern;
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
    }

    public static class NotYetImplementedException extends RuntimeException {

    }
}
