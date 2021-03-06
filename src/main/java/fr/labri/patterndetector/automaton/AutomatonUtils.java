package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.automaton.exception.RuleAutomatonException;
import fr.labri.patterndetector.runtime.expressions.IPredicate;
import fr.labri.patterndetector.rule.INegationBeginMarker;
import fr.labri.patterndetector.rule.INegationEndMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by William Braik on 7/23/2015.
 * <p>
 * Automaton utility methods.
 */
public final class AutomatonUtils {
    private final static Logger Logger = LoggerFactory.getLogger(AutomatonUtils.class);

    private AutomatonUtils() {
    }

    /**
     * Performs the copy of a rule automaton.
     *
     * @param automaton The rule automaton to copy.
     * @return A copy of the rule automaton.
     */
    public static IRuleAutomaton copy(IRuleAutomaton automaton) {
        IRuleAutomaton automatonCopy = new RuleAutomaton();
        doCopy(automaton.getInitialState(), automatonCopy);

        return automatonCopy;
    }

    /**
     * Recursive function called by the copy() method to perform the automaton copy.
     *
     * @param currentState  The current state to copy.
     * @param automatonCopy The automaton copy.
     * @return A copy of the current state.
     */
    private static IState doCopy(IState currentState, IRuleAutomaton automatonCopy) {
        IState stateCopy = new State();
        stateCopy.setFinal(currentState.isFinal());
        stateCopy.setInitial(currentState.isInitial());

        try {
            if (stateCopy.isInitial()) {
                automatonCopy.setInitialState(stateCopy);
            } else if (stateCopy.isFinal()) {
                automatonCopy.addFinalState(stateCopy);
            } else {
                automatonCopy.addState(stateCopy, currentState.getLabel());
            }

            currentState.getTransitions().forEach(t -> {
                IState target = t.getTarget();

                if (automatonCopy.getState(target.getLabel()) == null) {
                    target = doCopy(target, automatonCopy);
                    stateCopy.registerTransition(target, t.getLabel(), t.getType());
                } else {
                    stateCopy.registerTransition(automatonCopy.getState(target.getLabel()), t.getLabel(), t.getType());
                }
            });
        } catch (RuleAutomatonException e) {
            Logger.error("Automaton copy failed : " + e.getMessage() + "\n"
                    + e.getRuleAutomaton());

            throw new RuntimeException("Automaton copy failed : " + e.getMessage() + "\n"
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
        IRuleAutomaton powersetAutomaton = new RuleAutomaton();
        Set<IState> initialStateSet = new HashSet<>();
        initialStateSet.add(automaton.getInitialState());
        initialStateSet = extendStateSet(initialStateSet, new HashSet<>());
        IState initialState = new State();
        Map<Set<String>, IState> allStateSets = new HashMap<>();
        allStateSets.put(initialStateSet.stream().map(IState::getLabel).collect(Collectors.toSet()), initialState);

        try {
            powersetAutomaton.setInitialState(initialState);
            doPowerset(initialStateSet, allStateSets, powersetAutomaton);
        } catch (RuleAutomatonException e) {
            Logger.error("Powerset failed : " + e.getMessage() + "\n" + e.getRuleAutomaton());

            throw new RuntimeException("Powerset failed : " + e.getMessage() + "\n"
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
    private static void doPowerset(Set<IState> currentStateSet, Map<Set<String>, IState> allStateSets,
                                   IRuleAutomaton finalAutomaton) {
        Map<String, Set<IState>> targetStateSets = new HashMap<>();
        Map<String, TransitionType> transitionTypes = new HashMap<>();
        Map<String, ArrayList<IPredicate>> predicates = new HashMap<>();
        Map<String, ArrayList<INegationBeginMarker>> negationStartMarkers = new HashMap<>();
        Map<String, ArrayList<INegationEndMarker>> negationStopMarkers = new HashMap<>();
        Map<String, Integer> matchBufferPositions = new HashMap<>();

        for (IState state : currentStateSet) {
            for (ITransition t : state.getTransitions()) {
                if (!Transition.LABEL_EPSILON.equals(t.getLabel())) {
                    Set<IState> stateSet = targetStateSets.get(t.getLabel());
                    if (stateSet == null) {
                        stateSet = new HashSet<>();
                    }

                    // FIXME Potential bugs if two transitions with the same label originate from the same state set ;
                    // FIXME In that case, the transition picked last "wins" (its attribute overwrites the previous ones)
                    stateSet.add(t.getTarget());
                    targetStateSets.put(t.getLabel(), stateSet);
                    transitionTypes.put(t.getLabel(), t.getType());
                    predicates.put(t.getLabel(), t.getPredicates());
                    negationStartMarkers.put(t.getLabel(), t.getNegationBeginMarkers());
                    negationStopMarkers.put(t.getLabel(), t.getNegationEndMarkers());
                    matchBufferPositions.put(t.getLabel(), t.getMatchbufferPosition());
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
                // New state set found, create a new state which corresponds to it
                targetState = new State();

                for (IState state : targetStateSet) {
                    state.getActions().forEach(targetState::addAction);
                }

                if (isStateSetFinal(targetStateSet)) {
                    finalAutomaton.addFinalState(targetState);
                } else {
                    finalAutomaton.addState(targetState);
                }

                allStateSets.put(targetStateSet.stream().map(IState::getLabel).collect(Collectors.toSet()), targetState);

                doPowerset(targetStateSet, allStateSets, finalAutomaton);
            }

            // Create a transition to the target state set with the attributes which correspond to the label.
            IState currentState = allStateSets.get(currentStateSet.stream().map(IState::getLabel).collect(
                    Collectors.toSet()));
            currentState.registerTransition(targetState, label, transitionTypes.get(label))
                    .setPredicates(predicates.get(label))
                    .setNegationBeginMarkers(negationStartMarkers.get(label))
                    .setNegationEndMarkers(negationStopMarkers.get(label))
                    .setMatchbufferPosition(matchBufferPositions.get(label));
        });
    }

    /**
     * Extend a state set by absorbing the target states of all outgoing epsilon transitions.
     * Used by the Powerset algorithm.
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

        // Recursion : if new states were added to the state set, the state set potentially needs to be extended again.
        if (statesAdded) {
            extendedStateSet = extendStateSet(extendedStateSet, checkedStates);
        }

        return extendedStateSet;
    }

    /**
     * Check whether the state set if final, i.e. contains at least one final state.
     *
     * @param stateSet The state set to check.
     * @return A boolean stating whether the state set contains a final state.
     */
    private static boolean isStateSetFinal(Set<IState> stateSet) {
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