package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.runtime.predicates.IPredicate;
import fr.labri.patterndetector.runtime.predicates.INacBeginMarker;
import fr.labri.patterndetector.runtime.predicates.INacEndMarker;

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
    private ArrayList<INacBeginMarker> _startNacMarkers;
    private ArrayList<INacEndMarker> _stopNacMarkers;

    public Transition(IState source, IState target, String label, TransitionType type) {
        _source = source;
        _target = target;
        _label = label;
        _type = type;

        _predicates = new ArrayList<>();
        _startNacMarkers = new ArrayList<>();
        _stopNacMarkers = new ArrayList<>();
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
    public ArrayList<INacBeginMarker> getNacBeginMarkers() {
        return _startNacMarkers;
    }

    @Override
    public ArrayList<INacEndMarker> getNacEndMarkers() {
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
    public ITransition setNacBeginMarkers(ArrayList<INacBeginMarker> nacBeginMarkers) {
        _startNacMarkers = nacBeginMarkers;

        return this;
    }

    @Override
    public ITransition setNacEndMarkers(ArrayList<INacEndMarker> nacEndMarkers) {
        _stopNacMarkers = nacEndMarkers;

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
