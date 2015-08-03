package fr.labri.patterndetector.automaton;

import java.util.Map;

/**
 * Created by William Braik on 7/23/2015.
 */
public final class AutomatonUtils {

    private AutomatonUtils() {
    }

    public static IAutomaton powerset(IAutomaton automaton) {
        return automaton; // TODO if a state has 2 transitions with distinct
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
}