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
    // TODO enum StateType : a state only has 1 type, either INITIAL,NORMAL,RESET or FINAL

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
    public void registerTransition(IState target, String label, TransitionType type) throws Exception {
        if (_final) {
            throw new Exception("Can't add transitions to a final state !");
        } else if (_transitions.get(label) != null) {
            throw new Exception("A transition for " + label + " already exists !");
        } else {
            _transitions.put(label, new Transition(this, target, label, type));
        }
    }

    @Override
    public ITransition pickTransition(IEvent event) {
        ITransition t = _transitions.get(event.getType());
        // If the event doesn't match any transition from this state,
        // check if the state has a negative transition that could match.
        if (t == null) {
            t = _transitions.get(Transition.LABEL_NEGATION);
        }

        return t;
    }

    @Override
    public ITransition getTransitionByLabel(String label) {
        return _transitions.get(label);
    }

    @Override
    public String toString() {
        return _label;
    }
}
