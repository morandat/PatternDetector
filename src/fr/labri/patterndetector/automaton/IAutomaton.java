package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.IEvent;

import java.util.Collection;
import java.util.Set;

/**
 * Created by William Braik on 6/28/2015.
 */
public interface IAutomaton {

    public IState getCurrentState();

    public IState getInitialState();

    public Set<IState> getStates();

    public IState getFinalState();

    public Collection<IEvent> getBuffer();

    public void registerInitialState(IState s) throws Exception;  //TODO AutomatonException;

    public void registerState(IState s);

    public void registerFinalState(IState s) throws Exception; //TODO AutomatonException;

    public void fire(IEvent e) throws Exception; //TODO AutomatonException

    public void reset();
}
