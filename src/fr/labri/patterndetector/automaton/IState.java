package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.EventType;

/**
 * Created by William Braik on 6/28/2015.
 */
public interface IState {

    public StateType getType();

    public String getLabel();

    public IState next(EventType e) throws Exception; //TODO StateException

    public void registerTransition(EventType e, IState s);

    public void setFinal(boolean isFinal);

    public boolean isFinal();
}
