package fr.labri.patterndetector.automaton.exception;

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wbraik on 10/25/2015.
 * <p>
 * Automaton exception.
 */
public class RuleAutomatonException extends RuntimeException {

    private final Logger Logger = LoggerFactory.getLogger(RuleAutomatonException.class);

    private IRuleAutomaton _ruleAutomaton;

    public RuleAutomatonException(IRuleAutomaton ruleAutomaton, String message) {
        super(message);
        _ruleAutomaton = ruleAutomaton;

        Logger.error(message);
    }

    public IRuleAutomaton getRuleAutomaton() {
        return _ruleAutomaton;
    }
}
