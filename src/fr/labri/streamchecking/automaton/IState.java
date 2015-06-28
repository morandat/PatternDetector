package fr.labri.streamchecking.automaton;

import fr.labri.streamchecking.EventType;

/**
 * Created by William Braik on 6/28/2015.
 */
public interface IState {

    public StateType getType();

    public String getLabel();

    public IState next(EventType e) throws Exception; //TODO StateException

    public void registerTransition(EventType e, IState s);

    public boolean isFinal();
}
