package fr.labri.patterndetector.rules;

/**
 * Created by William Braik on 6/25/2015.
 */
public abstract class AbstractBinaryRule extends AbstractRule implements IBinaryRule {

    protected IRule _left;
    protected IRule _right;

    public AbstractBinaryRule(String symbol, IRule left, IRule right) {
        super(symbol);
        _left = left;
        _right = right;
    }

    @Override
    public IRule getLeftRule() {
        return _left;
    }

    @Override
    public IRule getRightRule() {
        return _right;
    }

    @Override
    public String toString() {
        return "(" + _left.toString() + " " + _symbol + " " + _right.toString() + ")";
    }
}
