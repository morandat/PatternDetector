package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.EventType;
import fr.labri.patterndetector.IEvent;

import java.util.Map;

/**
 * Created by William Braik on 6/28/2015.
 */
public interface IState {

    public String getLabel();

    public Map<EventType, IState> getTransitions();

    public boolean isFinal();

    public boolean isTake();

    public void setLabel(String label);

    public void setFinal(boolean isFinal);

    public void setTake(boolean take);

    public void registerTransition(EventType e, IState s);

    public IState next(IEvent e) throws Exception; //TODO StateException
}
