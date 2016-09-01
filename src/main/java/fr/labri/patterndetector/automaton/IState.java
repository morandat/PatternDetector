package fr.labri.patterndetector.automaton;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by William Braik on 6/28/2015.
 * <p>
 * Base interface for rule automaton states.
 */
public interface IState {

    String getLabel();

    Set<ITransition> getTransitions();

    boolean isInitial();

    boolean isFinal();

    ArrayList<Runnable> getActions();

    void setLabel(String label);

    void setInitial(boolean initial);

    void setFinal(boolean isFinal);

    void addAction(Runnable action);

    ITransition registerTransition(IState target, String label, TransitionType type);

    void registerEpsilonTransition(IState target);

    void registerStarTransition(IState target, TransitionType type);

    void removeTransition(String label);

    /**
     * Given an event, pick a transition that matches all the event properties (i.e. its label, its predicates,
     * and its clock guards).
     *
     * @param eventType The event type.
     * @return A transition that matches all the event properties, or null if no transitions match.
     * In a non-deterministic rule automaton, if several transitions match, one is picked randomly.
     */
    ITransition pickTransition(String eventType);

    void performActions();
}
