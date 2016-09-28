package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.automaton.IRuleAutomaton;

import java.io.Serializable;

/**
 * Created by wbraik on 5/18/2016.
 */
public class AutomatonRunnerFactory implements Serializable {

    public IAutomatonRunner getRunner(AutomatonRunnerType runnerType, IRuleAutomaton automaton) {
        switch (runnerType) {
            case Deterministic:
                return new DeterministicRunner(automaton);
            case NonDeterministicMatchAll:
                return new NonDeterministicRunner(automaton, RunnerMatchStrategy.MatchAll);
            case NonDeterministicMatchFirst:
                return new NonDeterministicRunner(automaton, RunnerMatchStrategy.MatchFirst);
            case NonDeterministicMatchLast:
                return new NonDeterministicRunner(automaton, RunnerMatchStrategy.MatchLast);
            default:
                throw new RuntimeException("Unknown runner type. Cannot instantiate runner");
        }
    }
}
