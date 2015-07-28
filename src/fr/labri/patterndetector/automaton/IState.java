package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.IEvent;

import java.util.Map;

/**
 * Created by William Braik on 6/28/2015.
 */
public interface IState {

    String getLabel();

    Map<String, ITransition> getTransitions();

    boolean isInitial();

    boolean isFinal();

    void setLabel(String label);

    void setInitial(boolean initial);

    void setFinal(boolean isFinal);

    void registerTransition(IState target, String label, TransitionType type) throws Exception;

    ITransition pickTransition(IEvent event);

    ITransition getTransitionByLabel(String label);
}
