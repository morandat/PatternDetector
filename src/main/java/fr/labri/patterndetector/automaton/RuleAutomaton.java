package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.automaton.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by William Braik on 6/28/2015.
 * <p>
 * A rule's automaton.
 * A type of timed automaton. Contains clocks for each event type.
 */

public class RuleAutomaton implements IRuleAutomaton {

    private final Logger logger = LoggerFactory.getLogger(RuleAutomaton.class);

    private IState _initialState;
    private Map<String, IState> _finalStates;
    private Map<String, IState> _connectionStates;
    private Map<String, IState> _states;

    public RuleAutomaton() {
        _states = new HashMap<>();
        _finalStates = new HashMap<>();
        _connectionStates = new HashMap<>();
    }

    @Override
    public IState getInitialState() {
        return _initialState;
    }

    @Override
    public IState getState(String label) {
        return _states.get(label);
    }

    @Override
    public Set<IState> getStates() {
        Set<IState> states = new HashSet<>();
        _states.values().forEach(states::add);

        return states;
    }

    @Override
    public IState getFinalState(String label) {
        return _finalStates.get(label);
    }

    @Override
    public Set<IState> getFinalStates() {
        Set<IState> finalStates = new HashSet<>();
        _finalStates.values().forEach(finalStates::add);

        return finalStates;
    }

    @Override
    public IState getConnectionState(String label) {
        return _connectionStates.get(label);
    }

    @Override
    public Set<IState> getConnectionStates() {
        Set<IState> connectionStates = new HashSet<>();
        _connectionStates.values().forEach(connectionStates::add);

        return connectionStates;
    }

    @Override
    public Set<IState> getAllStates() {
        Set<IState> states = getStates();
        states.add(_initialState);
        states.addAll(_finalStates.values());

        return states;
    }

    @Override
    public Collection<ITransition> getTransitions() {
        Set<ITransition> transitions = new HashSet<>();
        transitions.addAll(_initialState.getTransitions());
        _states.values().forEach(state -> transitions.addAll(state.getTransitions()));
        _finalStates.values().forEach(finalState -> transitions.addAll(finalState.getTransitions()));

        return transitions;
    }

    @Override
    public void setInitialState(IState s) throws RuleAutomatonException {
        if (_initialState != null) {
            throw new RuleAutomatonException(this, "Initial state already set");
        }
        s.setLabel(State.LABEL_INITIAL);
        s.setFinal(false);
        s.setInitial(true);
        _initialState = s;
    }

    @Override
    public void addState(IState s) {
        s.setLabel(Integer.toString(_states.size()));
        s.setInitial(false);
        s.setFinal(false);
        _states.put(s.getLabel(), s);
    }

    @Override
    public void addState(IState s, String label) {
        s.setLabel(label);
        s.setInitial(false);
        s.setFinal(false);
        _states.put(s.getLabel(), s);
    }

    @Override
    public void addFinalState(IState s) {
        s.setLabel(State.LABEL_FINAL + _finalStates.size());
        s.setInitial(false);
        s.setFinal(true);
        _finalStates.put(s.getLabel(), s);
    }

    @Override
    public void addFinalState(IState s, String label) {
        s.setLabel(label);
        s.setInitial(false);
        s.setFinal(true);
        _finalStates.put(s.getLabel(), s);
    }

    @Override
    public void addConnectionState(IState s) {
        _connectionStates.put(s.getLabel(), s);
    }

    @Override
    public void validate() throws NoInitialStateException, NoFinalStateException, UnreachableStatesException, NonDeterministicException {
        if (_initialState == null)
            throw new NoInitialStateException(this);

        if (_finalStates.isEmpty())
            throw new NoFinalStateException(this);

        for (IState finalState : getFinalStates()) {
            if (finalState.getTransitions().size() > 0)
                throw new UnreachableStatesException(this);
        }

        // Check that all states have exactly one transition for each different label (deterministic)
        for (IState state : getAllStates()) {
            long numDistinctTransitionLabels = state.getTransitions().stream().map(ITransition::getLabel).distinct().count();
            if (numDistinctTransitionLabels != state.getTransitions().size())
                throw new NonDeterministicException(this);
        }
    }

    @Override
    public String toString() {
        StringBuilder transitions = new StringBuilder("[");
        if (_initialState != null) {
            transitions.append(" (").append(_initialState).append(",").append(_initialState.getTransitions()).append(")");
        }
        for (IState state : _states.values()) {
            transitions.append(" (").append(state).append(",").append(state.getTransitions()).append(")");
        }
        for (IState finalState : _finalStates.values()) {
            transitions.append(" (").append(finalState).append(",").append(finalState.getTransitions()).append(")");
        }
        transitions.append(" ]");

        return transitions.toString();
    }
}
