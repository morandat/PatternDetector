package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.automaton.executor.IEvent;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by William Braik on 6/28/2015.
 * <p>
 * A state of a rule's automaton. Can be initial, final, or regular.
 */
public interface IState {

    String getLabel();

    Set<ITransition> getTransitions();

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

    ITransition pickTransition(IEvent event) throws Exception; // TODO NotDeterministicException

    ITransition getTransitionByLabel(String label) throws Exception; // TODO NotDeterministicException;
}
