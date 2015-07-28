package fr.labri.patterndetector.automaton;

import java.util.Map;

/**
 * Created by William Braik on 7/23/2015.
 */
public final class AutomatonUtils {

    private AutomatonUtils() {
    }

    public static IAutomaton powerset(IAutomaton automaton) {
        return automaton; // TODO
    }

    public static IAutomaton copy(IAutomaton automaton) {
        IAutomaton copy = new Automaton();
        startCopy(automaton.getInitialState(), copy);

        return copy;
    }

    private static IState startCopy(IState s, IAutomaton copy) {
        IState s1 = new State();
        s1.setFinal(s.isFinal());
        s1.setInitial(s.isInitial());

        try {
            if (s1.isInitial()) {
                copy.registerInitialState(s1);
            } else if (s1.isFinal()) {
                copy.registerFinalState(s1);
            } else {
                copy.registerState(s1);
            }


            for (Map.Entry<String, ITransition> entry : s.getTransitions().entrySet()) {
                ITransition t = entry.getValue();
                IState s2 = t.getTarget();
                if (copy.getStateByLabel(s2.getLabel()) == null) {
                    s2 = startCopy(s2, copy);
                }

                s1.registerTransition(s2, t.getLabel(), t.getType());
            }
        } catch (Exception e) {
            System.err.println("An error occured during the copy of the automaton (" + e.getMessage() + ")");
        }

        return s1;
    }
}