package fr.labri.patterndetector.rule.visitors;

import fr.labri.patterndetector.rule.ICompositeRule;
import fr.labri.patterndetector.rule.ITerminalRule;

/**
 * Created by wbraik on 19/11/15.
 */

@Deprecated
public class DefaultTraversal extends AbstractRuleVisitor {
    @Override
    public void visit(ITerminalRule terminalRule) {
    }

    @Override
    public void visit(ICompositeRule compositeRule) {
        compositeRule.getChildRules().forEach(rule -> rule.accept(this));
    }
}
