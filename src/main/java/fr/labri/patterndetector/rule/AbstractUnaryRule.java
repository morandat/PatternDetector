package fr.labri.patterndetector.rule;

/**
 * Created by William Braik on 6/28/2015.
 * <p>
 * Default behavior for unary rules.
 */
public abstract class AbstractUnaryRule extends AbstractCompositeRule implements IUnaryRule {

    protected IRule _childRule;

    public AbstractUnaryRule(String symbol, IRule childRule) {
        super(symbol, childRule);
        _childRule = childRule;
    }

    @Override
    public IRule getChildRule() {
        return _childRule;
    }
}
