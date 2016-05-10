package fr.labri.patterndetector.automaton.exception;

import fr.labri.patterndetector.automaton.IRuleAutomaton;

/**
 * Created by will on 26/10/15.
 */
public class UnreachableStatesException extends RuleAutomatonException {

    public UnreachableStatesException(IRuleAutomaton ruleAutomaton) {
        super(ruleAutomaton, "Invalid automaton : some states are unreachable");
    }
}
