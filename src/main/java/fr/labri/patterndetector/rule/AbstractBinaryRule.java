package fr.labri.patterndetector.rule;

/**
 * Created by William Braik on 6/25/2015.
 * <p>
 * Default behavior for binary rules.
 */
public abstract class AbstractBinaryRule extends AbstractCompositeRule implements IBinaryRule {

    protected IRule _leftChildRule;
    protected IRule _rightChildRule;

    public AbstractBinaryRule(String symbol, IRule leftChildRule, IRule rightChildRule) {
        super(symbol, leftChildRule, rightChildRule);
        _leftChildRule = leftChildRule;
        _rightChildRule = rightChildRule;
    }

    @Override
    public IRule getLeftChildRule() {
        return _leftChildRule;
    }

    @Override
    public IRule getRightChildRule() {
        return _rightChildRule;
    }
}
