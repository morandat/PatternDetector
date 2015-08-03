package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.IEvent;

import java.util.*;

/**
 * Created by William Braik on 6/28/2015.
 */
public class Automaton implements IAutomaton {

    protected IState _initialState;
    protected IState _finalState;
    protected Map<String, IState> _states;
    protected IState _currentState;
    protected ArrayList<IEvent> _buffer;

    public Automaton() {
        _states = new HashMap<>();
        _buffer = new ArrayList<>();
    }

    @Override
    public IState getCurrentState() {
        return _currentState;
    }

    @Override
    public IState getInitialState() {
        return _initialState;
    }

    @Override
    public IState getStateByLabel(String label) {
        if (State.LABEL_INITIAL.equals(label)) return _initialState;
        else if (State.LABEL_FINAL.equals(label)) return _finalState;
        else return _states.get(label);
    }

    @Override
    public Map<String, IState> getStates() {
        return _states;
    }

    @Override
    public IState getFinalState() {
        return _finalState;
    }

    @Override
    public Collection<IEvent> getBuffer() {
        return _buffer;
    }

    @Override
    public void registerInitialState(IState s) throws Exception {
        if (_initialState != null) {
            throw new Exception("An initial state has already been set !");
        }
        s.setLabel(State.LABEL_INITIAL);
        s.setInitial(true);
        _initialState = s;
    }

    @Override
    public void registerState(IState s) {
        s.setLabel(Integer.toString(_states.size()));
        _states.put(s.getLabel(), s);
    }

    @Override
    public void registerFinalState(IState s) throws Exception {
        if (_finalState != null) {
            throw new Exception("A final state has already been set !");
        }
        s.setLabel(State.LABEL_FINAL);
        s.setFinal(true);
        _finalState = s;
    }

    @Override
    public void fire(IEvent e) throws Exception {
        if (_initialState != null) {
            // Initialize current state if needed
            if (_currentState == null) {
                _currentState = _initialState;
            }

            System.out.println("Current state : " + _currentState);

            if (_currentState.isFinal()) {
                throw new Exception("Final state has already been reached ! Ignoring : " + e);
            } else {
                ITransition t = _currentState.pickTransition(e);

                if (t != null) {
                    System.out.println("Transitioning : " + t + " (" + e + ")");

                    // Action to perform on the transition
                    switch (t.getType()) {
                        case TRANSITION_APPEND:
                            _buffer.add(e);
                            break;
                        case TRANSITION_OVERWRITE:
                            _buffer.clear();
                            _buffer.add(e);
                            break;
                        case TRANSITION_DROP:
                    }

                    // Update current state
                    _currentState = _currentState.pickTransition(e).getTarget();

                    if (_currentState.isFinal()) {
                        // If the final state has been reached, post the found pattern and reset the automaton
                        post(_buffer);
                        reset();
                        System.out.println("Final state reached");
                    }
                } else {
                    System.out.println("Can't transition ! (" + e + ")");
                    reset();
                }
            }
        } else {
            throw new Exception("Initial state not set !");
        }
    }

    @Override
    public String toString() {
        StringBuilder transitions = new StringBuilder();
        if (_initialState != null) {
            transitions.append("[ (").append(_initialState).append(",").append(_initialState.getTransitions()).append(") ");
        }
        for (IState state : _states.values()) {
            transitions.append("(").append(state).append(",").append(state.getTransitions()).append(") ");
        }
        if (_finalState != null) {
            transitions.append("(").append(_finalState).append(",").append(_finalState.getTransitions()).append(") ]");
        }


        return transitions.toString();
    }

    @Override
    public void reset() {
        _currentState = _initialState;
        _buffer.clear();
        System.out.println("Automaton reset");
    }

    public void post(Collection<IEvent> pattern) {
        System.out.println("*** PATTERN FOUND : " + pattern + " ***");
    }
}
