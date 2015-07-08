package fr.labri.patterndetector.rules;

/**
 * Created by William Braik on 6/25/2015.
 */
public abstract class AbstractRule implements IRule {

    protected RuleType _type;
    protected String _symbol;
    protected TimeConstraint _tc;

    public AbstractRule(RuleType type, String symbol) {
        _type = type;
        _symbol = symbol;
        _tc = null;
    }

    public AbstractRule(RuleType type, String symbol, TimeConstraint tc) {
        _type = type;
        _symbol = symbol;
        _tc = tc;
    }

    @Override
    public RuleType getType() {
        return _type;
    }

    @Override
    public String getSymbol() {
        return _symbol;
    }

    @Override
    public TimeConstraint getTimeConstraint() {
        return _tc;
    }
}
