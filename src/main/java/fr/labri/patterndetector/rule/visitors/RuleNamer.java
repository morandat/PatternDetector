package fr.labri.patterndetector.rule.visitors;

import fr.labri.patterndetector.rule.*;

/**
 * Created by wbraik on 19/11/15.
 * <p>
 * Classic depth first traversal
 */

public final class RuleNamer {

    static private int _ruleCounter = 0;

    private RuleNamer() {
    }

    public static void nameRules(IRule rule) {
        _ruleCounter = 0;
        RuleNamer.RuleNamerVisitor visitor = new RuleNamer.RuleNamerVisitor();
        rule.accept(visitor);
    }

    private static class RuleNamerVisitor extends AbstractRuleVisitor {

        @Override
        public void visit(AbstractUnaryRule unaryRule) {
            unaryRule.getChildRule().accept(new RuleNamer.RuleNamerVisitor());
        }

        @Override
        public void visit(AbstractBinaryRule binaryRule) {
            binaryRule.getLeftChildRule().accept(new RuleNamer.RuleNamerVisitor());
            binaryRule.getRightChildRule().accept(new RuleNamer.RuleNamerVisitor());
        }

        @Override
        public void visit(Atom atom) {
            atom.setName("$" + _ruleCounter++);
        }
    }
}
