package fr.labri.patterndetector.automaton;

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

    public static IRuleAutomaton copy(IRuleAutomaton automaton) {
        IRuleAutomaton automatonCopy = new RuleAutomaton(automaton.getRule());
        startCopy(automaton.getInitialState(), automatonCopy);

        return automatonCopy;
    }

    private static IState startCopy(IState currentState, IRuleAutomaton automatonCopy) {
        IState stateCopy = new State();
        stateCopy.setFinal(currentState.isFinal());
        stateCopy.setInitial(currentState.isInitial());

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
                try {
                    if (automatonCopy.getStateByLabel(target.getLabel()) == null) {
                        target = startCopy(target, automatonCopy);
                        stateCopy.registerTransition(target, t.getLabel(), t.getType(), t.getClockConstraint());
                    } else {
                        stateCopy.registerTransition(automatonCopy.getStateByLabel(target.getLabel()), t.getLabel(), t.getType(), t.getClockConstraint());
                    }
                } catch (Exception e) {
                    logger.error("An error occured during the copy of the automaton (" + e.getMessage() + ")");
                }
            });
        } catch (Exception e) {
            logger.error("An error occured during the copy of the automaton (" + e.getMessage() + ")");
        }

        return stateCopy;
    }

    /**
     * Powerset construction algorithm : transform a NFA into a DFA.
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
        } catch (Exception e) {
            logger.error("An error occured during Powerset (" + e.getMessage() + ")");
        }

        return powersetAutomaton;
    }

    private static void startPowerset(Set<IState> currentStateSet, Map<Set<String>, IState> allStateSets, IRuleAutomaton finalAutomaton) {
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
            IState targetState = allStateSets.get(targetStateSet.stream().map(IState::getLabel).collect(Collectors.toSet()));

            if (targetState == null) {
                // New state set detected, must create new target state
                targetState = new State();

                if (isFinalStateSet(targetStateSet)) {
                    try {
                        finalAutomaton.setFinalState(targetState);
                    } catch (Exception e) {
                        logger.error("An error occured during Powerset (" + e.getMessage() + ")");
                    }
                } else {
                    finalAutomaton.addState(targetState);
                }

                allStateSets.put(targetStateSet.stream().map(IState::getLabel).collect(Collectors.toSet()), targetState);

                startPowerset(targetStateSet, allStateSets, finalAutomaton);
            }

            IState currentState = allStateSets.get(currentStateSet.stream().map(IState::getLabel).collect(Collectors.toSet()));
            currentState.registerTransition(targetState, label, transitionTypes.get(label), clockGuards.get(label), predicates.get(label));
        });
    }

    /**
     * Extend the state set by running the epsilon transitions from each state of the set.
     * Used by the powerset construction algorithm.
     **/
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

        /* If new states have been added to the state set, we need to check those for epsilon transitions, so keep the
        extension going. If no epsilon transitions were found in any state, we can stop the extension. */
        if (statesAdded) {
            extendedStateSet = extendStateSet(extendedStateSet, checkedStates);
        }

        return extendedStateSet;
    }

    /**
     * Check if the state set contains at least one accepting state
     **/
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