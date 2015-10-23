package fr.labri.patterndetector.rules;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by William Braik on 6/28/2015.
 * <p>
 * Default behavior for unary rules.
 */
public abstract class AbstractUnaryRule extends AbstractRule implements IUnaryRule {

    protected IRule _childRule;

    public AbstractUnaryRule(String symbol, IRule r) {
        super(symbol);
        _childRule = r;
    }

    @Override
    public IRule getChildRule() {
        return _childRule;
    }

    @Override
    public String toString() {
        return "(" + _childRule.toString() + ")" + _symbol;
    }

    @Override
    public Collection<IRule> getChildRules() {
        Set<IRule> childRules = new HashSet<>();
        childRules.add(_childRule);
        return childRules;
    }
}
