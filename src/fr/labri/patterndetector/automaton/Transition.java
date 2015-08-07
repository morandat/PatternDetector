package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.rules.TimeConstraint;

/**
 * Created by William Braik on 7/27/2015.
 */
public class Transition implements ITransition {

    public static final String LABEL_EPSILON = "$";
    public static final String LABEL_NEGATION = "*";

    private IState _source;
    private IState _target;
    private String _label;
    TransitionType _type;
    TimeConstraint _timeConstraint;

    public Transition(IState source, IState target, String label, TransitionType type) {
        _source = source;
        _target = target;
        _label = label;
        _type = type;
        _timeConstraint = null;
    }

    public Transition(IState source, IState target, String label, TransitionType type, TimeConstraint timeConstraint) {
        _source = source;
        _target = target;
        _label = label;
        _type = type;
        _timeConstraint = timeConstraint;
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
    public TimeConstraint getTimeConstraint() {
        return _timeConstraint;
    }

    @Override
    public void setTimeConstraint(TimeConstraint timeConstraint) {
        _timeConstraint = timeConstraint;
    }

    @Override
    public String toString() {
        return _source + ":" + _label + " => " + _target + " [" + _type + "]";
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
