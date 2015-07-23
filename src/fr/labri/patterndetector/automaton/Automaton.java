package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.IEvent;

import java.util.*;

/**
 * Created by William Braik on 6/28/2015.
 */
public class Automaton implements IAutomaton {

    protected IState _initialState;
    protected IState _finalState;
    protected Set<IState> _states;
    protected IState _currentState;
    protected ArrayList<IEvent> _buffer;

    public Automaton() {
        _states = new HashSet<>();
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
    public Set<IState> getStates() {
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
        s.setLabel("I");
        _initialState = s;
        s.setInitial(true);
    }

    @Override
    public void registerState(IState s) {
        s.setLabel(Integer.toString(_states.size()));
        _states.add(s);
    }

    @Override
    public void registerFinalState(IState s) throws Exception {
        if (_finalState != null) {
            throw new Exception("A final state has already been set !");
        }
        s.setLabel("F");
        _finalState = s;
        s.setFinal(true);
    }

    @Override
    public void fire(IEvent e) throws Exception {
        if (_initialState != null) {
            // Initialize current state if needed
            if (_currentState == null) {
                _currentState = _initialState;
            }
//
            if (_currentState.isFinal()) {
                throw new Exception("Final state has already been reached ! Ignoring : " + e);
            } else {
                _currentState = _currentState.next(e);
                System.out.println(_currentState + (_currentState.isTake() ? " [take]" : " [ignore]"));

                // If the current state is of type "Take", collect e
                if (_currentState.isTake()) {
                    _buffer.add(e);
                }
                // If the final state has been reached, post the found pattern and reset the automaton
                if (_currentState.isFinal()) {
                    post(_buffer);
                    reset();
                    System.out.println("Final state reached, automaton reset");
                }
            }
        } else {
            throw new Exception("Initial state not set !");
        }
    }

    @Override
    public String toString() {
        StringBuilder transitions = new StringBuilder("[ (" + _initialState + "," + _initialState.getTransitions() + "," + _initialState.isTake() + ") ");
        for (IState state : _states) {
            transitions.append("(" + state + "," + state.getTransitions() + "," + state.isTake() + ") ");
        }
        transitions.append("(" + _finalState + "," + _finalState.getTransitions() + "," + _finalState.isTake() + ") ]");

        return transitions.toString();
    }

    @Override
    public void reset() {
        _currentState = _initialState;
        _buffer.clear();
    }

    public void post(Collection<IEvent> pattern) {
        System.out.println("Pattern found ! " + pattern);
    }
}
