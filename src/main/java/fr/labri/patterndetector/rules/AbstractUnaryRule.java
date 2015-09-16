package fr.labri.patterndetector.rules;

/**
 * Created by William Braik on 6/28/2015.
 */
public abstract class AbstractUnaryRule extends AbstractRule implements IUnaryRule {

    protected IRule _rule;

    public AbstractUnaryRule(RuleType type, String symbol, IRule r) {
        super(type, symbol);
        _rule = r;
    }

    @Override
    public RuleType getType() {
        return _type;
    }

    @Override
    public IRule getRule() {
        return _rule;
    }

    @Override
    public String toString() {
        return "(" + _rule.toString() + ")" + _symbol;
    }
}
