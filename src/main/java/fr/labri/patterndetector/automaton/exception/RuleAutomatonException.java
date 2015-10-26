package fr.labri.patterndetector.automaton.exception;

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.rules.IRule;

/**
 * Created by wbraik on 10/25/2015.
 * <p>
 * Automaton exception.
 */
public class RuleAutomatonException extends Exception {

    private IRuleAutomaton _ruleAutomaton;

    public RuleAutomatonException(IRuleAutomaton ruleAutomaton, String message) {
        super(message);
        _ruleAutomaton = ruleAutomaton;
    }

    public IRuleAutomaton getRuleAutomaton() {
        return _ruleAutomaton;
    }

    public IRule getRule() {
        return _ruleAutomaton.getRule();
    }
}
