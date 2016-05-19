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
        RuleNamer.RuleNamerVisitor visitor = new RuleNamer.RuleNamerVisitor();
        rule.accept(visitor);
    }

    private static class RuleNamerVisitor extends AbstractRuleVisitor {

        @Override
        public void visit(AbstractUnaryRule unaryRule) {
            RuleNamer.nameRules(unaryRule);
        }

        @Override
        public void visit(AbstractBinaryRule binaryRule) {
            RuleNamer.nameRules(binaryRule.getLeftChildRule());
            RuleNamer.nameRules(binaryRule.getRightChildRule());
        }

        @Override
        public void visit(Atom atom) {
            atom.setName("$" + _ruleCounter++);
        }
    }
}
