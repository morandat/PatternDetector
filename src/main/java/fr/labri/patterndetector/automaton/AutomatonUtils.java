package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.automaton.exception.RuleAutomatonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by William Braik on 7/23/2015.
 * <p>
 * Automaton utility methods.
 */
public final class AutomatonUtils {
    private final static Logger logger = LoggerFactory.getLogger(AutomatonUtils.class);

    private AutomatonUtils() {
    }

    /**
     * Performs a copy of a rule automaton.
     *
     * @param automaton The rule automaton to copy.
     * @return A copy of the rule automaton.
     */
    public static IRuleAutomaton copy(IRuleAutomaton automaton) {
        IRuleAutomaton automatonCopy = new RuleAutomaton(automaton.getRule());
        startCopy(automaton.getInitialState(), automatonCopy);

        return automatonCopy;
    }

    /**
     * Recursive function called by the copy() method to perform the automaton copy.
     *
     * @param currentState  The current state to copy.
     * @param automatonCopy The automaton copy.
     * @return A copy of the current state.
     */
    private static IState startCopy(IState currentState, IRuleAutomaton automatonCopy) {
        IState stateCopy = new State();

        try {
            if (stateCopy.isInitial()) {
                automatonCopy.setInitialState(stateCopy);
            } else if (stateCopy.isFinal()) {
                automatonCopy.setFinalState(stateCopy);
            } else {
                automatonCopy.addState(stateCopy);
            }

            currentState.getTransitions().forEach(t -> {
                IState target = t.getTarget();

                if (automatonCopy.getState(target.getLabel()) == null) {
                    target = startCopy(target, automatonCopy);
                    stateCopy.registerTransition(target, t.getLabel(), t.getType(), t.getClockConstraint());
                } else {
                    stateCopy.registerTransition(automatonCopy.getState(target.getLabel()), t.getLabel(), t.getType(),
                            t.getClockConstraint());
                }
            });
        } catch (RuleAutomatonException e) {
            logger.error("Automaton copy failed : " + e.getRule() + " (" + e.getMessage() + ")\n"
                    + e.getRuleAutomaton());

            throw new RuntimeException("Automaton copy failed : " + e.getRule() + " (" + e.getMessage() + ")\n"
                    + e.getRuleAutomaton());
        }

        return stateCopy;
    }

    /**
     * The Powerset construction algorithm. Transforms a NFA into a DFA.
     *
     * @param automaton The NFA to transform.
     * @return The corresponding DFA.
     */
    public static IRuleAutomaton powerset(IRuleAutomaton automaton) {
        IRuleAutomaton powersetAutomaton = new RuleAutomaton(automaton.getRule());
        Set<IState> initialStateSet = new HashSet<>();
        initialStateSet.add(automaton.getInitialState());
        IState initialState = new State();
        Map<Set<String>, IState> allStateSets = new HashMap<>();
        allStateSets.put(initialStateSet.stream().map(IState::getLabel).collect(Collectors.toSet()), initialState);

        try {
            powersetAutomaton.setInitialState(initialState);
            startPowerset(initialStateSet, allStateSets, powersetAutomaton);
        } catch (RuleAutomatonException e) {
            logger.error("Powerset failed : " + e.getRule() + " (" + e.getMessage() + ")\n" + e.getRuleAutomaton());

            throw new RuntimeException("Powerset failed : " + e.getRule() + " (" + e.getMessage() + ")\n"
                    + e.getRuleAutomaton());
        }

        return powersetAutomaton;
    }

    /**
     * Recursive function called by the powerset() method to perform the Powerset.
     *
     * @param currentStateSet The current state set.
     * @param allStateSets    All state sets.
     * @param finalAutomaton  The final DFA.
     */
    private static void startPowerset(Set<IState> currentStateSet, Map<Set<String>, IState> allStateSets,
                                      IRuleAutomaton finalAutomaton) {
        Map<String, Set<IState>> targetStateSets = new HashMap<>();
        Map<String, TransitionType> transitionTypes = new HashMap<>();
        Map<String, ClockGuard> clockGuards = new HashMap<>();
        Map<String, Map<String, Predicate<Integer>>> predicates = new HashMap<>();

        for (IState state : currentStateSet) {
            for (ITransition t : state.getTransitions()) {
                if (!Transition.LABEL_EPSILON.equals(t.getLabel())) {
                    Set<IState> stateSet = targetStateSets.get(t.getLabel());
                    if (stateSet == null) {
                        stateSet = new HashSet<>();
                    }

                    stateSet.add(t.getTarget());
                    targetStateSets.put(t.getLabel(), stateSet);
                    transitionTypes.put(t.getLabel(), t.getType());
                    clockGuards.put(t.getLabel(), t.getClockConstraint());
                    predicates.put(t.getLabel(), t.getPredicates());
                }
            }
        }

        targetStateSets.forEach((label, targetStateSet) -> {
            Set<IState> extendedStateSet = extendStateSet(targetStateSet, new HashSet<>());
            targetStateSets.put(label, extendedStateSet);
        });

        targetStateSets.forEach((label, targetStateSet) -> {
            IState targetState = allStateSets.get(targetStateSet.stream().map(IState::getLabel).collect(
                    Collectors.toSet()));

            if (targetState == null) {
                // New state set detected, must create new target state
                targetState = new State();

                if (isFinalStateSet(targetStateSet)) {
                    try {
                        finalAutomaton.setFinalState(targetState);
                    } catch (RuleAutomatonException e) {
                        logger.error("Powerset failed : " + e.getRule() + " (" + e.getMessage() + ")\n"
                                + e.getRuleAutomaton());

                        throw new RuntimeException("Powerset failed : " + e.getRule() + " (" + e.getMessage() + ")\n"
                                + e.getRuleAutomaton());
                    }
                } else {
                    finalAutomaton.addState(targetState);
                }

                allStateSets.put(targetStateSet.stream().map(IState::getLabel).collect(Collectors.toSet()), targetState);

                startPowerset(targetStateSet, allStateSets, finalAutomaton);
            }

            IState currentState = allStateSets.get(currentStateSet.stream().map(IState::getLabel).collect(
                    Collectors.toSet()));
            currentState.registerTransition(targetState, label, transitionTypes.get(label), clockGuards.get(label),
                    predicates.get(label));
        });
    }

    /**
     * Used by Powerset to extend a state set by absorbing the target states of all outgoing epsilon transitions.
     *
     * @param stateSet      The state set to extend.
     * @param checkedStates The states that have already been absorbed into the state set.
     * @return The extended state set.
     */
    private static Set<IState> extendStateSet(Set<IState> stateSet, Set<String> checkedStates) {
        Set<IState> extendedStateSet = new HashSet<>();
        extendedStateSet.addAll(stateSet);
        boolean statesAdded = false;

        for (IState s : stateSet) {
            if (!checkedStates.contains(s.getLabel())) {
                for (ITransition t : s.getTransitions()) {
                    if (Transition.LABEL_EPSILON.equals(t.getLabel())) {
                        extendedStateSet.add(t.getTarget());
                        statesAdded = true;
                    }
                }

                checkedStates.add(s.getLabel());
            }
        }

        /* If new states have been added to the state set, we need to check those new states for epsilon transitions,
        and keep extending the state set if needed. If no new epsilon transitions are found, we can stop extending. */
        if (statesAdded) {
            extendedStateSet = extendStateSet(extendedStateSet, checkedStates);
        }

        return extendedStateSet;
    }

    /**
     * Check whether the state set contains at least one final state.
     *
     * @param stateSet The state set to check for final states.
     * @return True if the state set contains a final state, False otherwise.
     */
    private static boolean isFinalStateSet(Set<IState> stateSet) {
        boolean isFinal = false;

        for (IState s : stateSet) {
            if (s.isFinal()) {
                isFinal = true;
                break;
            }
        }

        return isFinal;
    }
}