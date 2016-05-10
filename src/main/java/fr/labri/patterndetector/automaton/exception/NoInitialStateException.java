package fr.labri.patterndetector.automaton.exception;

import fr.labri.patterndetector.automaton.IRuleAutomaton;

/**
 * Created by will on 26/10/15.
 */
public class NoInitialStateException extends RuleAutomatonException {

    public NoInitialStateException(IRuleAutomaton ruleAutomaton) {
        super(ruleAutomaton, "Invalid automaton : initial state not set");
    }
}
