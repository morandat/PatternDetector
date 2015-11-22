package fr.labri.patterndetector.compiler;

import fr.labri.patterndetector.rules.ICompositeRule;
import fr.labri.patterndetector.rules.ITerminalRule;

/**
 * Created by wbraik on 19/11/15.
 */
public class DefaultTraversal extends AbstractRuleVisitor {
    @Override
    public void visit(ITerminalRule terminalRule) {
    }

    @Override
    public void visit(ICompositeRule compositeRule) {
        compositeRule.getChildRules().forEach(rule -> rule.accept(this));
    }
}
