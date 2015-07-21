package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.IEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by William Braik on 6/28/2015.
 */
public class Automaton implements IAutomaton {

    protected IState _initialState;
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
    public Collection<IEvent> getBuffer() {
        return _buffer;
    }

    @Override
    public void registerInitialState(IState s) throws Exception {
        _initialState = s;
        registerState(s);
    }

    @Override
    public void registerState(IState s) {
        s.setLabel(Integer.toString(_states.size()));
        _states.add(s);
    }

    @Override
    public void registerFinalState(IState s) {
        s.setFinal(true);
        registerState(s);
    }

    @Override
    public IState fire(IEvent e) throws Exception {
        if (_initialState != null) {
            // Initialize current state if needed
            if (_currentState == null) {
                _currentState = _initialState;
            }

            if (_currentState.isFinal()) {
                throw new Exception("Final state already reached ! Ignoring : " + e); //TODO AutomatonException
            } else {
                _currentState = _currentState.next(e);
                // If the current state is of type "Take", collect e
                if (_currentState.isTake()) {
                    _buffer.add(e);
                }

                // Check if the final state has been reached
                if (_currentState.isFinal()) {
                    System.out.println("Final state reached !");
                }

                return _currentState;
            }
        } else {
            throw new Exception("Initial state not set !"); //TODO AutomatonException
        }
    }

    @Override
    public String toString() {
        return _buffer.toString();
    }

    @Override
    public void reset() {
        _currentState = _initialState;
        _buffer = new ArrayList<>();

        System.out.println("Automaton reset");
    }
}
