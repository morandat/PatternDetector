package fr.labri.patterndetector.lang;

import xtc.util.Pair;

import java.util.concurrent.TimeUnit;

/**
 * Created by morandat on 10/05/2016.
 */
public class AST {

    public static Rule newRule(String name) {
        return null;
    }
    public static Transition newTransition() {
        return null;
    }
    public static Transition newNak() {
        return null;
    }
    public static Pattern newSimplePattern() {
        return null;
    }
    public static Pattern newCompositePattern(Pattern p) {
        return null;
    }
    public static Pattern newSimplePattern(String name) {
        return null;
    }
    public static Pattern newKleene(Pattern p) {
        return null;
    }
    public static TimeValue newTimeValue(String value, TimeUnit unit) {
        int v = Integer.parseInt(value);
        return null;
    }

    public static class Rule {
        public void appendConstraint(Constraint c) {
        }

        public void appendPattern(Pattern m) {
        }
    }

    public static class Pattern {
        public void addAlias(String name) {
        }

        public void addTransitions(Pair<Transition> transitions) {
            for(Transition t: transitions)
                appendTransition(t);
        }

        public void appendTransition(Transition t) {
        }
    }

    public static class Constraint {
    }

    public static class Transition {
    }

    public static class TimeValue {

    }
}
