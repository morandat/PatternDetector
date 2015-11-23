package fr.labri.patterndetector.automaton;

import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by William Braik on 7/27/2015.
 */
public class Transition implements ITransition {

    public static final String LABEL_EPSILON = "$";
    public static final String LABEL_STAR = "*";

    private IState _source;
    private IState _target;
    private String _label;
    TransitionType _type;
    ClockGuard _clockGuard;
    Map<String, Predicate<Integer>> _predicates;

    public Transition(IState source, IState target, String label, TransitionType type) {
        _source = source;
        _target = target;
        _label = label;
        _type = type;
    }

    public Transition(IState source, IState target, String label, TransitionType type, ClockGuard clockGuard) {
        _source = source;
        _target = target;
        _label = label;
        _type = type;
        _clockGuard = clockGuard;
    }

    public Transition(IState source, IState target, String label, TransitionType type,
                      Map<String, Predicate<Integer>> predicates) {
        _source = source;
        _target = target;
        _label = label;
        _type = type;
        _predicates = predicates;
    }

    public Transition(IState source, IState target, String label, TransitionType type, ClockGuard clockGuard,
                      Map<String, Predicate<Integer>> predicates) {
        _source = source;
        _target = target;
        _label = label;
        _type = type;
        _clockGuard = clockGuard;
        _predicates = predicates;
    }

    @Override
    public IState getSource() {
        return _source;
    }

    @Override
    public IState getTarget() {
        return _target;
    }

    @Override
    public String getLabel() {
        return _label;
    }

    @Override
    public TransitionType getType() {
        return _type;
    }

    @Override
    public ClockGuard getClockGuard() {
        return _clockGuard;
    }

    @Override
    public Predicate<Integer> getPredicate(String field) {
        return _predicates.get(field);
    }

    @Override
    public Map<String, Predicate<Integer>> getPredicates() {
        return _predicates;
    }

    @Override
    public boolean isEpsilon() {
        return _label.equals(Transition.LABEL_EPSILON);
    }

    @Override
    public boolean isStar() {
        return _label.equals(Transition.LABEL_STAR);
    }

    @Override
    public void setClockGuard(ClockGuard clockGuard) {
        _clockGuard = clockGuard;
    }

    @Override
    public void setClockGuard(String eventType, int value) {
        _clockGuard = new ClockGuard(eventType, value);
    }

    @Override
    public void setClockGuard(String eventType, int value, boolean lowerThan) {
        _clockGuard = new ClockGuard(eventType, value, lowerThan);
    }

    @Override
    public void setPredicate(String field, Predicate<Integer> predicate) {
        _predicates.put(field, predicate);
    }

    @Override
    public void setPredicates(Map<String, Predicate<Integer>> predicates) {
        _predicates = predicates;
    }

    @Override
    public String toString() {
        return _source + ":" + _label + " => " + _target + " [" + _type + "]"
                + (_clockGuard == null ? "" : " {" + _clockGuard + "}");
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Transition)) return false;

        Transition otherTransition = (Transition) other;
        return otherTransition.getLabel().equals(_label) && otherTransition.getSource().equals(_source)
                && otherTransition.getTarget().equals(_target) && otherTransition.getType().equals(_type);
    }
}
