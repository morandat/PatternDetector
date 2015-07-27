package fr.labri.patterndetector.automaton;

/**
 * Created by William Braik on 7/27/2015.
 */
public interface ITransition {

    IState getSource();

    IState getTarget();

    String getLabel();

    boolean isTake();
}
