package fr.labri.patterndetector.rules;

import java.util.Collection;

/**
 * Created by wbraik on 23/10/15.
 * <p>
 * Base interface for rules that are composed by smaller rules.
 */
public interface ICompositeRule extends IRule {

    Collection<IRule> getChildRules();
}
