package fr.labri.patterndetector.automaton;

import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by William Braik on 7/27/2015.
 */
public interface ITransition {

    IState getSource();

    IState getTarget();

    String getLabel();

    TransitionType getType();

    ClockGuard getClockConstraint();

    Predicate<Integer> getPredicateOnField(String field);

    Map<String, Predicate<Integer>> getPredicates();

    void setClockConstraint(ClockGuard clockGuard);

    void setClockConstraint(String eventType, int value);

    void setClockConstraint(String eventType, int value, boolean lowerThan);

    void setPredicateOnField(String field, Predicate<Integer> predicate); // TODO replace Integer by generic type '?'

    void setPredicates(Map<String, Predicate<Integer>> predicates);
}
