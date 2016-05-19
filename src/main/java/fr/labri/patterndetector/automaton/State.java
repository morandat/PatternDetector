package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.executor.IEvent;

import java.util.*;

/**
 * Created by William Braik on 6/28/2015.
 * <p>
 * A state of a rule automaton. Can be initial, final, or regular.
 */
public class State implements IState {

    public static final String LABEL_FINAL = "F";
    public static final String LABEL_INITIAL = "I";

    protected String _label;
    protected Map<String, List<ITransition>> _transitions;
    protected boolean _initial;
    protected boolean _final;
    protected ArrayList<Runnable> _actions;

    public State() {
        _label = null;
        _transitions = new HashMap<>();
        _final = false;
        _initial = false;
        _actions = new ArrayList<>();
    }

    @Override
    public String getLabel() {
        return _label;
    }

    @Override
    public Set<ITransition> getTransitions() {
        Set<ITransition> transitions = new HashSet<>();
        _transitions.values().forEach(list -> list.forEach(transitions::add));

        return transitions;
    }

    @Override
    public List<ITransition> getTransitionsByLabel(String label) {
        Collection<ITransition> transitions = _transitions.get(label);

        if (transitions == null)
            return null;

        return _transitions.get(label);
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
    public ArrayList<Runnable> getActions() {
        return _actions;
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
    public void addAction(Runnable action) {
        _actions.add(action);
    }

    @Override
    public ITransition registerTransition(IState target, String label, TransitionType type) {
        ITransition t = new Transition(this, target, label, type);

        if (_transitions.get(label) == null) {
            List<ITransition> list = new ArrayList<>();
            list.add(t);
            _transitions.put(label, list);
        } else {
            if (!_transitions.get(label).contains(t)) { // Check duplicate transitions
                // TODO throw DuplicateTransitionException?
                _transitions.get(label).add(t);
            }
        }

        return t;
    }

    @Override
    public void registerEpsilonTransition(IState target) {
        String label = Transition.LABEL_EPSILON;
        ITransition t = new Transition(this, target, label, TransitionType.TRANSITION_DROP);

        if (_transitions.get(label) == null) {
            List<ITransition> list = new ArrayList<>();
            list.add(t);
            _transitions.put(label, list);
        } else {
            if (!_transitions.get(label).contains(t)) { // Check duplicate transitions
                // TODO throw DuplicateTransitionException?
                _transitions.get(label).add(t);
            }
        }
    }

    @Override
    public void registerStarTransition(IState target, TransitionType type) {
        String label = Transition.LABEL_STAR;
        ITransition t = new Transition(this, target, label, type);

        if (_transitions.get(label) == null) {
            List<ITransition> list = new ArrayList<>();
            list.add(t);
            _transitions.put(label, list);
        } else {
            if (!_transitions.get(label).contains(t)) { // Check duplicate transitions
                // TODO throw DuplicateTransitionException
                _transitions.get(label).add(t);
            }
        }
    }

    @Override
    public void removeTransition(String label) {
        _transitions.remove(label);
    }

    @Override
    public ITransition pickTransition(IEvent event) {
        List<ITransition> transitions = _transitions.get(event.getType());
        if (transitions == null) {
            transitions = _transitions.get(Transition.LABEL_STAR);
        }

        ITransition t;
        if (transitions == null) {
            t = null;
        } else {
            // FIXME for now, pick first transition if several match (NFA)
            t = transitions.get(0);
        }

        return t;
    }

    @Override
    public void performActions() {
        _actions.forEach(Runnable::run);
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
