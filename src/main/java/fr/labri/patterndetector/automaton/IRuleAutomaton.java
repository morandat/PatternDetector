package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.automaton.exception.*;
import fr.labri.patterndetector.executor.IEvent;
import fr.labri.patterndetector.executor.IPatternObserver;
import fr.labri.patterndetector.rules.IRule;

import java.util.Collection;
import java.util.Set;

/**
 * Created by William Braik on 6/28/2015.
 * <p>
 * A rule's automaton.
 * A type of timed automaton. Contains clocks for each event type.
 */
public interface IRuleAutomaton {

    /**
     * Get the current state of the rule automaton.
     *
     * @return The current state of the rule automaton.
     */
    IState getCurrentState();

    /**
     * Get the initial state of the rule automaton.
     *
     * @return The initial state of the rule automaton.
     */
    IState getInitialState();

    /**
     * Lookup a state of the rule automaton by its label.
     *
     * @param label The state label.
     * @return The state mapped to the label, or null if no states are mapped to the label.
     */
    IState getState(String label);

    /**
     * Get regular states of the rule automaton (excluding the initial and final states).
     *
     * @return A set containing only the regular states of the rule automaton.
     */
    Set<IState> getStates();

    /**
     * Get the final state of the rule automaton.
     *
     * @return The final state of the rule automaton.
     */
    IState getFinalState();

    /**
     * Get all states of the rule automaton (including the initial and final states).
     *
     * @return A set containing all sets of the rule automaton.
     */
    Set<IState> getAllStates();

    Collection<IEvent> getMatchBuffer();

    Collection<ITransition> getTransitions();

    void setInitialState(IState s) throws RuleAutomatonException;

    void addState(IState s);

    void addState(IState s, String label);

    void setFinalState(IState s) throws RuleAutomatonException;

    void fire(IEvent e);

    void reset();

    /**
     * Register a pattern observer.
     *
     * @param observer The pattern observer to register.
     */
    void registerPatternObserver(IPatternObserver observer);

    /**
     * Action performed when a pattern is found (i.e. when the final state is reached)
     *
     * @param pattern The detected pattern.
     */
    void patternDetected(Collection<IEvent> pattern);

    /**
     * Check whether the rule automaton is valid, i.e. can be executed.
     * Must be called by the client to ensure safe usage of the automaton.
     */
    void validate() throws NoInitialStateException, NoFinalStateException, UnreachableStatesException, NonDeterministicException;

    /**
     * @return The corresponding powerset automaton of this automaton.
     */
    default IRuleAutomaton powerset() {
        return AutomatonUtils.powerset(this);
    }

    /**
     * @return A copy of this automaton.
     */
    default IRuleAutomaton copy() {
        return AutomatonUtils.copy(this);
    }
}
