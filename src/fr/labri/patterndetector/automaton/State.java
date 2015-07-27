package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.IEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by William Braik on 6/28/2015.
 */
public class State implements IState {

    public static final String LABEL_FINAL = "F";
    public static final String LABEL_INITIAL = "I";

    protected String _label;
    protected Map<String, ITransition> _transitions;
    protected boolean _initial;
    protected boolean _final;

    public State() {
        _label = null;
        _transitions = new HashMap<>();
        _final = false;
        _initial = false;
    }

    @Override
    public String getLabel() {
        return _label;
    }

    @Override
    public Map<String, ITransition> getTransitions() {
        return _transitions;
    }

    @Override
    public boolean isInitial() {
        return _initial;
    }

    @Override
    public boolean isFinal() {
        return _final;
    }

    @Override
    public void setLabel(String label) {
        _label = label;
    }

    @Override
    public void setInitial(boolean initial) {
        _initial = initial;
    }

    @Override
    public void setFinal(boolean isFinal) {
        _final = isFinal;
    }

    @Override
    public void registerTransition(IState target, String label, boolean take) throws Exception {
        if (_transitions.get(label) != null) {
            throw new Exception("A transition for " + label + " already exists !");
        } else {
            _transitions.put(label, new Transition(this, target, label, take));
        }
    }

    @Override
    public ITransition getTransition(IEvent e) {
        ITransition t = _transitions.get(e.getType());
        // If the event doesn't match any transition from this state,
        // check if the state has a negative transition that could match.
        if (t == null) {
            t = _transitions.get(AutomatonUtils.negativeTransitionLabel());
        }

        return t;
    }

    @Override
    public String toString() {
        return _label;
    }
}
