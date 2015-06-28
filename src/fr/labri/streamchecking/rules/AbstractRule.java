package fr.labri.streamchecking.rules;

/**
 * Created by William Braik on 6/25/2015.
 */
public abstract class AbstractRule implements IRule {

    protected RuleType _type;
    protected String _symbol;

    public AbstractRule(RuleType type, String symbol) {
        _type = type;
        _symbol = symbol;
    }

    @Override
    public RuleType getType() {
        return _type;
    }

    @Override
    public String getSymbol() {
        return _symbol;
    }
}
