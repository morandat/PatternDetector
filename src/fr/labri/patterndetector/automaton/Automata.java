package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.EventType;

import java.util.Map;

/**
 * Created by William Braik on 7/23/2015.
 */
public final class Automata {

    private Automata() {
    }

    public static IAutomaton powerset(IAutomaton automaton) {
        return automaton; // TODO
    }

    public static IAutomaton copy(IAutomaton automaton) {
        IAutomaton copy = new Automaton();

        startCopy(automaton.getInitialState(), copy);

        return copy;
    }

    private static IState startCopy(IState root, IAutomaton copy) {
        IState s = new State(root.isTake());
        s.setFinal(root.isFinal());
        s.setInitial(root.isInitial());

        for (Map.Entry<EventType, IState> entry : root.getTransitions().entrySet()) {
            IState s2 = startCopy(entry.getValue(), copy);
            s.registerTransition(entry.getKey(), s2);
        }

        try {
            if (s.isInitial()) {
                copy.registerInitialState(s);
            } else if (s.isFinal()) {
                copy.registerFinalState(s);
            } else {
                copy.registerState(s);
            }
        } catch (Exception e) {
            System.err.println("An error occured during the copy of the automaton (" + e.getMessage() + ")");
        }

        return s;
    }
}