package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.EventType;

import java.util.Set;

/**
 * Created by William Braik on 6/28/2015.
 */
public interface IAutomaton {

    public IState getCurrentState();

    public IState getInitialState();

    public void setInitialState(IState s);

    public Set<IState> getStates();

    public void registerState(IState s);

    public void registerFinalState(IState s);

    public IState fire(EventType e) throws Exception; //TODO AutomatonException
}
