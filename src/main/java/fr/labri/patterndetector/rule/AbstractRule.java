package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.rule.visitors.RuleStringifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by William Braik on 6/25/2015.
 * <p>
 * Composes sub-rules and/or atoms to form patterns.
 */
public abstract class AbstractRule implements IRule {

    // Concrete subclasses should log with their own class names
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected String _name;
    protected String _symbol;

    public AbstractRule(String symbol) {
        _symbol = symbol;
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
    public String toString() {
        return RuleStringifier.stringify(this);
    }
}
