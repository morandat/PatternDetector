package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.runtime.expressions.IPredicate;
import fr.labri.patterndetector.rule.INegationBeginMarker;
import fr.labri.patterndetector.rule.INegationEndMarker;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by William Braik on 7/27/2015.
 */
public class Transition implements ITransition, Serializable {

    public static final String LABEL_EPSILON = "$";
    public static final String LABEL_STAR = "*";

    private IState _source;
    private IState _target;
    private String _label;
    private TransitionType _type;
    private int _matchBufferPosition;
    private ArrayList<IPredicate> _predicates;
    private ArrayList<INegationBeginMarker> _negationStartMarkers;
    private ArrayList<INegationEndMarker> _negationStopMarkers;

    public Transition(IState source, IState target, String label, TransitionType type) {
        _source = source;
        _target = target;
        _label = label;
        _type = type;

        _predicates = new ArrayList<>();
        _negationStartMarkers = new ArrayList<>();
        _negationStopMarkers = new ArrayList<>();
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
    public String toString() {
        return _source + ":" + _label + " => " + _target + " [" + _type + "]";
    }

    @Override
    public int getMatchbufferPosition() {
        return _matchBufferPosition;
    }

    @Override
    public ArrayList<IPredicate> getPredicates() {
        return _predicates;
    }

    @Override
    public ArrayList<INegationBeginMarker> getNegationBeginMarkers() {
        return _negationStartMarkers;
    }

    @Override
    public ArrayList<INegationEndMarker> getNegationEndMarkers() {
        return _negationStopMarkers;
    }

    @Override
    public ITransition setMatchbufferPosition(int matchBufferKey) {
        _matchBufferPosition = matchBufferKey;

        return this;
    }

    @Override
    public ITransition setPredicates(ArrayList<IPredicate> predicates) {
        _predicates = predicates;

        return this;
    }

    @Override
    public ITransition setNegationBeginMarkers(ArrayList<INegationBeginMarker> negationBeginMarkers) {
        _negationStartMarkers = negationBeginMarkers;

        return this;
    }

    @Override
    public ITransition setNegationEndMarkers(ArrayList<INegationEndMarker> negationEndMarkers) {
        _negationStopMarkers = negationEndMarkers;

        return this;
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
