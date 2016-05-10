package fr.labri.patterndetector.rule;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wbraik on 26/11/15.
 */
public abstract class AbstractCompositeRule extends AbstractRule implements ICompositeRule {

    protected Set<IRule> _childRules = new HashSet<>();

    public AbstractCompositeRule(String symbol, IRule... childRules) {
        super(symbol);
        Collections.addAll(_childRules, childRules);
    }

    @Override
    public Collection<IRule> getChildRules() {
        return _childRules;
    }
}
