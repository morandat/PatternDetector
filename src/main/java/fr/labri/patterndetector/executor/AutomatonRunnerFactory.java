package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.automaton.IRuleAutomaton;

/**
 * Created by wbraik on 5/18/2016.
 */
public class AutomatonRunnerFactory {

    public IAutomatonRunner getRunner(AutomatonRunnerType runnerType, IRuleAutomaton automaton) {
        switch (runnerType) {
            case DFA:
                return new DFARunner(automaton);
            case NFA:
                return new NFARunner(automaton);
            default:
                throw new RuntimeException("Unknown runner type. Cannot instantiate runner");
        }
    }
}
