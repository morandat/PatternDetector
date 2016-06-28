package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.runtime.predicates.IPredicate;
import fr.labri.patterndetector.runtime.predicates.IStartNacMarker;
import fr.labri.patterndetector.runtime.predicates.IStopNacMarker;

import java.util.ArrayList;

/**
 * Created by William Braik on 7/27/2015.
 */
public class Transition implements ITransition {

    public static final String LABEL_EPSILON = "$";
    public static final String LABEL_STAR = "*";

    private IState _source;
    private IState _target;
    private String _label;
    private TransitionType _type;
    private String _matchBufferKey;
    private ArrayList<IPredicate> _predicates;
    private ArrayList<IStartNacMarker> _startNacMarkers;
    private ArrayList<IStopNacMarker> _stopNacMarkers;

    public Transition(IState source, IState target, String label, TransitionType type) {
        _source = source;
        _target = target;
        _label = label;
        _type = type;
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
    public String getMatchbufferKey() {
        return _matchBufferKey;
    }

    @Override
    public ArrayList<IPredicate> getPredicates() {
        return _predicates;
    }

    @Override
    public ArrayList<IStartNacMarker> getStartNacMarkers() {
        return _startNacMarkers;
    }

    @Override
    public ArrayList<IStopNacMarker> getStopNacMarkers() {
        return _stopNacMarkers;
    }

    @Override
    public ITransition setMatchBufferKey(String matchBufferKey) {
        _matchBufferKey = matchBufferKey;

        return this;
    }

    @Override
    public ITransition setPredicates(ArrayList<IPredicate> predicates) {
        _predicates = predicates;

        return this;
    }

    @Override
    public ITransition setStartNacMarkers(ArrayList<IStartNacMarker> startNacMarkers) {
        _startNacMarkers = startNacMarkers;

        return this;
    }

    @Override
    public ITransition setStopNacMarkers(ArrayList<IStopNacMarker> stopNacMarkers) {
        _stopNacMarkers = stopNacMarkers;

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
