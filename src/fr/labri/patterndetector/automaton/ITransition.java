package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.rules.TimeConstraint;

/**
 * Created by William Braik on 7/27/2015.
 */
public interface ITransition {

    IState getSource();

    IState getTarget();

    String getLabel();

    TransitionType getType();

    TimeConstraint getTimeConstraint();

    void setTimeConstraint(TimeConstraint timeConstraint);
}
