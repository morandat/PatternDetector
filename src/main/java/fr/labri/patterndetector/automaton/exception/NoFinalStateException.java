package fr.labri.patterndetector.automaton.exception;

import fr.labri.patterndetector.automaton.IRuleAutomaton;

/**
 * Created by will on 26/10/15.
 */
public class NoFinalStateException extends RuleAutomatonException {

    public NoFinalStateException(IRuleAutomaton ruleAutomaton) {
        super(ruleAutomaton, "Invalid automaton : final state not set");
    }
}
