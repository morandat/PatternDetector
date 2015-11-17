package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.exception.RuleAutomatonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by William Braik on 6/25/2015.
 * <p>
 * Composes sub-rules and/or atoms to form patterns.
 */
public abstract class AbstractRule implements IRule {

    // Concrete subclasses will log with their own class names
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected String _name;
    protected String _symbol;
    protected TimeConstraint _timeConstraint;
    protected String _connectionStateLabel;
    protected IRuleAutomaton _automaton;

    public AbstractRule(String symbol) {
        _symbol = symbol;
        _timeConstraint = null;
        _connectionStateLabel = null;
        _automaton = null;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }

    @Override
    public String getSymbol() {
        return _symbol;
    }

    @Override
    public TimeConstraint getTimeConstraint() {
        return _timeConstraint;
    }

    @Override
    public String getConnectionStateLabel() {
        return _connectionStateLabel;
    }

    @Override
    public IRule setTimeConstraint(TimeConstraint timeConstraint) {
        _timeConstraint = timeConstraint;

        // If the automaton isn't built yet, the clock guards will be created later on, at build time.
        // Otherwise, the clock guards need to be added immediately to the existing automaton.
        if (_automaton != null) {
            // TODO createClockConstraints()
        }

        return this;
    }

    @Override
    public IRule setTimeConstraint(int value) {
        return setTimeConstraint(new TimeConstraint(value));
    }

    @Override
    public final IRuleAutomaton getAutomaton() throws RuleAutomatonException {
        if (_automaton == null) {
            buildAutomaton();

            logger.debug(_automaton.toString());
        }

        return _automaton;
    }

    // TODO this method will disappear. Automaton construction code will be put in the compiler visitor.
    protected abstract void buildAutomaton() throws RuleAutomatonException;
}
