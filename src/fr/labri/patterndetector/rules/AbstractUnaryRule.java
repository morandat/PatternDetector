package fr.labri.patterndetector.rules;

/**
 * Created by William Braik on 6/28/2015.
 */
public abstract class AbstractUnaryRule extends AbstractRule implements IUnaryRule {

    protected IRule _r;

    public AbstractUnaryRule(RuleType type, String symbol, IRule r) {
        super(type, symbol);
        _r = r;
    }

    public AbstractUnaryRule(RuleType type, String symbol, IRule r, TimeConstraint tc) {
        super(type, symbol, tc);
        _r = r;
    }

    @Override
    public RuleType getType() {
        return _type;
    }

    @Override
    public IRule getRule() {
        return _r;
    }

    @Override
    public String toString() {
        return "(" + _r.toString() + ")" + _symbol;
    }
}
