package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.executor.IEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by William Braik on 6/28/2015.
 * <p>
 * Base interface for rule automaton states.
 */
public interface IState {

    String getLabel();

    Set<ITransition> getTransitions();

    /**
     * Get all transitions mapped to a given label.
     *
     * @param label A transition label.
     * @return The transitions mapped to the label, or null if no transitions are mapped to the label.
     */
    List<ITransition> getTransitionsByLabel(String label);

    boolean isInitial();

    boolean isFinal();

    IRuleAutomaton getAutomaton();

    void setLabel(String label);

    void setInitial(boolean initial);

    void setFinal(boolean isFinal);

    void setAutomaton(IRuleAutomaton automaton);

    void registerTransition(IState target, String label, TransitionType type);

    void registerTransition(IState target, String label, TransitionType type, ClockGuard clockGuard);

    void registerTransition(IState target, String label, TransitionType type,
                            Map<String, Predicate<Integer>> predicates);

    void registerTransition(IState target, String label, TransitionType type, ClockGuard clockGuard,
                            Map<String, Predicate<Integer>> predicates);

    void registerEpsilonTransition(IState target);

    void registerStarTransition(IState target, TransitionType type);

    void removeTransition(String label);

    /**
     * Given an event, pick a transition that matches all the event properties (i.e. its label, its predicates,
     * and its clock guards).
     *
     * @param event The event to pick a transition for.
     * @return A transition that matches all the event properties, or null if no transitions match.
     * In a non-deterministic rule automaton, if several transitions match, one is picked randomly.
     */
    ITransition pickTransition(IEvent event);
}
