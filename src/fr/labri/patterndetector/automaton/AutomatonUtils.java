package fr.labri.patterndetector.automaton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by William Braik on 7/23/2015.
 */
public final class AutomatonUtils {

    private AutomatonUtils() {
    }

    public static IRuleAutomaton copy(IRuleAutomaton automaton) {
        IRuleAutomaton automatonCopy = new RuleAutomaton(automaton.getRule());
        startCopy(automaton.getInitialState(), automatonCopy);

        return automatonCopy;
    }

    private static IState startCopy(IState state, IRuleAutomaton automatonCopy) {
        IState stateCopy = new State();
        stateCopy.setFinal(state.isFinal());
        stateCopy.setInitial(state.isInitial());

        try {
            if (stateCopy.isInitial() || stateCopy.isFinal()) {
                if (stateCopy.isInitial()) {
                    automatonCopy.registerInitialState(stateCopy);
                }
                if (stateCopy.isFinal()) {
                    automatonCopy.registerFinalState(stateCopy);
                }
            } else {
                automatonCopy.registerState(stateCopy);
            }

            state.getTransitions().forEach(t -> {
                IState target = t.getTarget();
                try {
                    if (automatonCopy.getStateByLabel(target.getLabel()) == null) {
                        target = startCopy(target, automatonCopy);
                        stateCopy.registerTransition(target, t.getLabel(), t.getType());
                    } else {
                        stateCopy.registerTransition(automatonCopy.getStateByLabel(target.getLabel()), t.getLabel(), t.getType());
                    }
                } catch (Exception e) {
                    System.err.println("An error occured during the copy of the automaton (" + e.getMessage() + ")");
                }
            });
        } catch (Exception e) {
            System.err.println("An error occured during the copy of the automaton (" + e.getMessage() + ")");
        }

        return stateCopy;
    }

    public static IRuleAutomaton powerset(IRuleAutomaton automaton) {
        // Powerset construction algorithm : remove the epsilon transitions from the NFA to obtain a DFA.
        IRuleAutomaton finalAutomaton = new RuleAutomaton(automaton.getRule());
        Set<IState> initialStateSet = new HashSet<>();
        initialStateSet.add(automaton.getInitialState());
        IState initialState = new State();
        Map<Set<String>, IState> allStateSets = new HashMap<>();
        allStateSets.put(initialStateSet.stream().map(IState::getLabel).collect(Collectors.toSet()), initialState);

        try {
            finalAutomaton.registerInitialState(initialState);
            startPowerset(initialStateSet, allStateSets, finalAutomaton);
        } catch (Exception e) {
            System.err.println("An error occured during Powerset (" + e.getMessage() + ")");
            e.printStackTrace();
        }

        return finalAutomaton;
    }

    private static void startPowerset(Set<IState> currentStateSet, Map<Set<String>, IState> allStateSets, IRuleAutomaton finalAutomaton) {
        Map<String, Set<IState>> targetStateSets = new HashMap<>();
        Map<String, TransitionType> transitionTypes = new HashMap<>();

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
                        finalAutomaton.registerFinalState(targetState);
                    } catch (Exception e) {
                        System.err.println("An error occured during Powerset (" + e.getMessage() + ")");
                        e.printStackTrace();
                    }
                } else {
                    finalAutomaton.registerState(targetState);
                }

                allStateSets.put(targetStateSet.stream().map(IState::getLabel).collect(Collectors.toSet()), targetState);

                startPowerset(targetStateSet, allStateSets, finalAutomaton);
            }

            IState currentState = allStateSets.get(currentStateSet.stream().map(IState::getLabel).collect(Collectors.toSet()));
            currentState.registerTransition(targetState, label, transitionTypes.get(label));
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