package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.Event;
import fr.labri.patterndetector.EventType;

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
    protected ArrayList<EventType> _buffer;

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
    public void setInitialState(IState s) {
        _currentState = _initialState = s;
    }

    @Override
    public Set<IState> getStates() {
        return _states;
    }

    @Override
    public void registerState(IState s) {
        _states.add(s);
    }

    @Override
    public void registerFinalState(IState s) {
        s.setFinal(true);
        _states.add(s);
    }

    @Override
    public IState fire(EventType e) throws Exception {
        if (_currentState != null) {
            if (_currentState.isFinal()) {
                throw new Exception("Final state already reached ! Ignoring : " + e); //TODO AutomatonException
            } else {
                _currentState = _currentState.next(e);
                // If the current state is of type "Take", collect e
                if (StateType.STATE_TAKE.equals(_currentState.getType())) {
                    _buffer.add(e);
                }
                // Check if the final state has been reached
                if (_currentState.isFinal()) {
                    System.out.println("Final state reached !");
                    //TODO if final state reached, reset the automata
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

    public Collection<EventType> getBuffer() {
        return _buffer;
    }
}
