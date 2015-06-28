package fr.labri.streamchecking.automaton;

import fr.labri.streamchecking.EventType;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by William Braik on 6/28/2015.
 */
public class Automaton implements IAutomaton {

    protected IState _initialState;
    protected Set<IState> _states;
    protected IState _currentState;

    public Automaton() {
        _states = new HashSet<>();
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
    public IState fire(EventType e) throws Exception {
        if (_currentState != null) {
            _currentState = _currentState.next(e);
            return _currentState;
        } else {
            throw new Exception("INITIAL STATE NOT SET"); //TODO AutomatonException
        }
    }

    @Override
    public String toString() {
        return "";
    }
}
