package fr.labri.patterndetector.automaton.exception;

import fr.labri.patterndetector.automaton.IRuleAutomaton;

/**
 * Created by will on 26/10/15.
 */
public class NonDeterministicException extends RuleAutomatonException {

    public NonDeterministicException(IRuleAutomaton ruleAutomaton) {
        super(ruleAutomaton, "Invalid automaton : non-deterministic");
    }
}
