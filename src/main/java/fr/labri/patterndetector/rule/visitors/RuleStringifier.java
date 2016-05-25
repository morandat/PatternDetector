package fr.labri.patterndetector.rule.visitors;

import fr.labri.patterndetector.rule.*;

/**
 * Created by wbraik on 26/11/15.
 * <p>
 * Used to stringify any rule.
 */
public final class RuleStringifier {

    private RuleStringifier() {
    }

    public static String stringify(IRule rule) {
        RuleStringifierVisitor visitor = new RuleStringifierVisitor();
        rule.accept(visitor);

        return visitor.getRuleString();
    }

    private static class RuleStringifierVisitor extends AbstractRuleVisitor {

        private String _ruleString;

        @Override
        public void visit(Atom atom) {
            _ruleString = atom.getEventType() + "(" + atom.getName() + ")";
        }

        @Override
        public void visit(Kleene kleene) {
            _ruleString = kleene.getEventType() + kleene.getSymbol() + "(" + kleene.getName() + ")";
        }

        @Override
        public void visit(AbstractUnaryRule unaryRule) {
            RuleStringifierVisitor visitor = new RuleStringifierVisitor();
            unaryRule.getChildRule().accept(visitor);

            _ruleString = "(" + visitor.getRuleString() + ")" + unaryRule.getSymbol();
        }

        @Override
        public void visit(AbstractBinaryRule binaryRule) {
            RuleStringifierVisitor leftVisitor = new RuleStringifierVisitor();
            binaryRule.getLeftChildRule().accept(leftVisitor);
            RuleStringifierVisitor rightVisitor = new RuleStringifierVisitor();
            binaryRule.getRightChildRule().accept(rightVisitor);

            _ruleString = leftVisitor.getRuleString() + " "
                    + binaryRule.getSymbol()
                    + " " + rightVisitor.getRuleString();
        }

        public String getRuleString() {
            return _ruleString;
        }
    }
}
