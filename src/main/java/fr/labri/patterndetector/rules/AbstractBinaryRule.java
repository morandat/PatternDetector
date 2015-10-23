package fr.labri.patterndetector.rules;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by William Braik on 6/25/2015.
 * <p>
 * Default behavior for binary rules.
 */
public abstract class AbstractBinaryRule extends AbstractRule implements IBinaryRule {

    protected IRule _leftChild;
    protected IRule _rightChild;

    public AbstractBinaryRule(String symbol, IRule left, IRule right) {
        super(symbol);
        _leftChild = left;
        _rightChild = right;
    }

    @Override
    public IRule getLeftChildRule() {
        return _leftChild;
    }

    @Override
    public IRule getRightChildRule() {
        return _rightChild;
    }

    @Override
    public String toString() {
        return "(" + _leftChild.toString() + " " + _symbol + " " + _rightChild.toString() + ")";
    }

    @Override
    public Collection<IRule> getChildRules() {
        Set<IRule> childRules = new HashSet<>();
        childRules.add(_leftChild);
        childRules.add(_rightChild);
        return childRules;
    }
}
