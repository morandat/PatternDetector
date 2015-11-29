package fr.labri.patterndetector.rule.visitors;

import fr.labri.patterndetector.rule.*;

/**
 * Created by wbraik on 19/11/15.
 * <p>
 * Classic depth first traversal
 */

public class DefaultTraversal extends AbstractRuleVisitor {

    public void visit(AbstractTerminalRule terminalRule) {
    }

    public void visit(AbstractCompositeRule compositeRule) {
        compositeRule.getChildRules().forEach(r -> r.accept(this));
    }
}
