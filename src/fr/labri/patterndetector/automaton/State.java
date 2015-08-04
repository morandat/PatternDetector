package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.IEvent;

import java.util.*;

/**
 * Created by William Braik on 6/28/2015.
 */
public class State implements IState {

    public static final String LABEL_FINAL = "F";
    public static final String LABEL_INITIAL = "I";

    protected String _label;
    protected Map<String, List<ITransition>> _transitions;
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
    public Set<ITransition> getTransitions() {
        Set<ITransition> set = new HashSet<>();
        _transitions.values().forEach(list -> list.forEach(set::add));

        return set;
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
    public void registerTransition(IState target, String label, TransitionType type) {
        ITransition t = new Transition(this, target, label, type);

        if (_transitions.get(label) == null) {
            List<ITransition> list = new ArrayList<>();
            list.add(t);
            _transitions.put(label, list);
        } else {
            if (!_transitions.get(label).contains(t)) { // Check duplicate transitions
                _transitions.get(label).add(t);
            }
        }
    }

    @Override
    public void removeTransition(String label) {
        _transitions.remove(label);
    }

    @Override
    public ITransition pickTransition(IEvent event) throws Exception {
        List<ITransition> list = _transitions.get(event.getType());
        if (list == null) {
            list = _transitions.get(Transition.LABEL_NEGATION);
        }

        if (list == null) {
            return null;
        } else if (list.size() > 1) {
            throw new Exception("The automaton is not deterministic !");
        } else {
            return list.get(0);
        }
    }

    @Override
    public ITransition getTransitionByLabel(String label) throws Exception {
        Collection<ITransition> transitions = _transitions.get(label);
        if (transitions == null)
            return null;
        else {
            if (transitions.size() > 1) {
                throw new Exception("The automaton is not deterministic !");
            } else {
                return _transitions.get(label).get(0);
            }
        }
    }

    @Override
    public String toString() {
        return _label;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof State)) return false;

        State otherState = (State) other;
        return otherState.getLabel().equals(_label) && otherState.isInitial() == _initial
                && otherState.isFinal() == _final && otherState.getTransitions().equals(getTransitions());
    }
}
