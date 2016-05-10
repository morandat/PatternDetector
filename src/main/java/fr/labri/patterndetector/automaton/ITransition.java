package fr.labri.patterndetector.automaton;

import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by William Braik on 7/27/2015.
 *
 * Interface for the transitions of a rule automaton.
 */
public interface ITransition {

    IState getSource();

    IState getTarget();

    String getLabel();

    TransitionType getType();

    boolean isEpsilon();

    boolean isStar();
}
