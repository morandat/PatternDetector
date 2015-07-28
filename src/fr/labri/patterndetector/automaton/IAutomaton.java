package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.IEvent;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by William Braik on 6/28/2015.
 */
public interface IAutomaton {

    IState getCurrentState();

    IState getInitialState();

    IState getStateByLabel(String label);

    Map<String, IState> getStates();

    IState getFinalState();

    Collection<IEvent> getBuffer();

    void registerInitialState(IState s) throws Exception;  //TODO AutomatonException;

    void registerState(IState s);

    void registerFinalState(IState s) throws Exception; //TODO AutomatonException;

    void fire(IEvent e) throws Exception; //TODO AutomatonException

    void reset();
}
