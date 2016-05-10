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

    ClockGuard getClockGuard();

    Predicate<Integer> getPredicate(String field);

    Map<String, Predicate<Integer>> getPredicates();

    boolean isEpsilon();

    boolean isStar();

    void setClockGuard(ClockGuard clockGuard);

    void setClockGuard(String eventType, int value);

    void setClockGuard(String eventType, int value, boolean lowerThan);

    void setPredicate(String field, Predicate<Integer> predicate); // TODO replace Integer by generic type '?'

    void setPredicates(Map<String, Predicate<Integer>> predicates);
}
