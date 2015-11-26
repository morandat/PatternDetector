package fr.labri.patterndetector.rule.visitors;

import fr.labri.patterndetector.rule.*;

/**
 * Created by wbraik on 26/11/15.
 * <p>
 * Used to stringify any rule.
 * FIXME What would happen for composite rules that are not unary or binary? Or terminals that are not atoms
 */
public class RuleStringifier {

    public String stringify(IRule rule) {
        RuleStringifierVisitor visitor = new RuleStringifierVisitor();
        rule.accept(visitor);

        return visitor.getRuleString();
    }

    class RuleStringifierVisitor extends AbstractRuleVisitor {

        private String _ruleString;

        @Override
        public void visit(Atom atom) {
            _ruleString = atom.getSymbol() + atom.getEventType();
        }

        @Override
        public void visit(Kleene kleene) {
            RuleStringifierVisitor visitor = new RuleStringifierVisitor();
            kleene.getChildRule().accept(visitor);

            _ruleString = "(" + visitor.getRuleString() + ")" + kleene.getSymbol();
        }


        public void visit(FollowedBy followedBy) {
            RuleStringifierVisitor leftVisitor = new RuleStringifierVisitor();
            followedBy.getLeftChildRule().accept(leftVisitor);
            RuleStringifierVisitor rightVisitor = new RuleStringifierVisitor();
            followedBy.getRightChildRule().accept(rightVisitor);

            _ruleString = leftVisitor.getRuleString() + " "
                    + followedBy.getSymbol()
                    + " " + rightVisitor.getRuleString();
        }

        public String getRuleString() {
            return _ruleString;
        }
    }
}
