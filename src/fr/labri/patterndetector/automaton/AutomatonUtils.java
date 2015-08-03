package fr.labri.patterndetector.automaton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by William Braik on 7/23/2015.
 */
public final class AutomatonUtils {

    private AutomatonUtils() {
    }

    public static IAutomaton copy(IAutomaton automaton) {
        IAutomaton automatonCopy = new Automaton();
        startCopy(automaton.getInitialState(), automatonCopy);

        return automatonCopy;
    }

    private static IState startCopy(IState state, IAutomaton automatonCopy) {
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

    public static IAutomaton powerset(IAutomaton automaton) {
        // Powerset construction algorithm : remove the epsilon transitions from the NFA to obtain a corresponding DFA.
        IAutomaton finalAutomaton = new Automaton();
        Set<IState> initialStateSet = new HashSet<>();
        IState initialState = new State();
        initialStateSet.add(initialState);

        try {
            finalAutomaton.registerInitialState(initialState);

            startPowerset(initialState, initialStateSet, finalAutomaton);
        } catch (Exception e) {
            System.err.println("An error occured during Powerset (" + e.getMessage() + ")");
        }

        return finalAutomaton;
    }

    private static IState startPowerset(IState currentState, Set<IState> currentStateSet, IAutomaton finalAutomaton) {
        Map<String, Set<IState>> stateSets = new HashMap<>();

        currentStateSet.forEach(state ->
                        state.getTransitions().forEach(t -> {
                            Set<IState> stateSet = stateSets.get(t.getLabel());
                            if (stateSet == null) {
                                stateSet = new HashSet<>();
                            }
                            stateSets.put(t.getLabel(), stateSet);
                        })
        );

        stateSets.values().forEach(stateSet -> extendStateSet(stateSet, new HashSet<>()));

        stateSets.forEach((label, stateSet) -> {
            IState newState = new State();
            finalAutomaton.registerState(newState);

            currentState.registerTransition(newState, label, );

            startPowerset(stateSet, finalAutomaton);
        });
    }

    /**
     * Extend the state set by running the epsilon transitions from each state of the set.
     * Used by the powerset construction algorithm.
     **/
    private static Set<IState> extendStateSet(Set<IState> stateSet, Set<String> checkedStates) {
        boolean statesAdded = false;

        for (IState s : stateSet) {
            if (!checkedStates.contains(s.getLabel())) {
                for (ITransition t : s.getTransitions()) {
                    if (Transition.LABEL_EPSILON.equals(t.getLabel())) {
                        stateSet.add(t.getTarget());
                        statesAdded = true;
                    }
                }

                checkedStates.add(s.getLabel());
            }
        }

        /* If new states have been added to the state set, we need to check those for epsilon transitions, so keep the
        extension going. If no epsilon transitions were found in any state, we can stop the extension. */
        if (statesAdded) {
            extendStateSet(stateSet, checkedStates);
        }

        return stateSet;
    }
}