package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.automaton.exception.*;
import fr.labri.patterndetector.executor.IEvent;
import fr.labri.patterndetector.executor.IPatternObserver;

import java.util.ArrayList;
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
     * Get the initial state of the rule automaton.
     *
     * @return The initial state of the rule automaton.
     */
    IState getInitialState();

    /**
     * Get a state of the rule automaton by its label.
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
     * Get a final state of the rule automaton by its label.
     *
     * @return One of the final states of the rule automaton.
     */
    IState getFinalState(String label);

    /**
     * Get a final state of the rule automaton by its label.
     *
     * @return One of the final states of the rule automaton.
     */
    Set<IState> getFinalStates();

    /**
     * Get the connection state's label from the rule's automaton.
     * The connection state is the state which is used by operators (such as FollowedBy) as a connection point between
     * the current rule's automaton and the following pattern's automaton.
     *
     * @return The connection state's label of the rule's automaton.
     */
    Set<IState> getConnectionStates();

    /**
     * Get a connection state of the rule automaton by its label.
     *
     * @return One of the connection states of the rule automaton.
     */
    IState getConnectionState(String label);

    /**
     * Get all states of the rule automaton (including the initial and final states).
     *
     * @return A set containing all sets of the rule automaton.
     */
    Set<IState> getAllStates();

    Collection<ITransition> getTransitions();

    void setInitialState(IState s) throws RuleAutomatonException;

    void addState(IState s);

    void addState(IState s, String label);

    void addFinalState(IState s);

    void addFinalState(IState s, String label);

    void addConnectionState(IState s);

    /**
     * Check whether the rule automaton is valid, i.e. can be executed.
     * Should be called by the client to ensure safe usage of the automaton.
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
