package fr.labri.patterndetector.rule;

import java.util.Collection;

/**
 * Created by wbraik on 23/10/15.
 * <p>
 * Base interface for composite rules, i.e. rules composed by smaller rules.
 */
public interface ICompositeRule extends IRule {

    Collection<IRule> getChildRules();
}
