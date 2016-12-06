package fr.labri.patterndetector.rule.visitors;

import fr.labri.patterndetector.rule.*;
import fr.labri.patterndetector.rule.INegationBeginMarker;

/**
 * Created by wbraik on 19/11/15.
 * <p>
 * Sets rule numbers
 */

public final class RulesNumberer {

    public static int numberRule(IRule rule) {
        RulesNumbererVisitor visitor = new RulesNumbererVisitor();
        rule.accept(visitor);

        return visitor._ruleNumber;
    }

    private static class RulesNumbererVisitor extends AbstractRuleVisitor {
        private int _ruleNumber = 0;

        @Override
        public void visit(AbstractUnaryRule unaryRule) {
            unaryRule.getChildRule().accept(this);
        }

        @Override
        public void visit(AbstractBinaryRule binaryRule) {
            binaryRule.getLeftChildRule().accept(this);
            binaryRule.getRightChildRule().accept(this);
        }

        @Override
        public void visit(AbstractTerminalRule terminalRule) {
            for (INegationBeginMarker negationBeginMarker : terminalRule.getNegationBeginMarkers()) {
                negationBeginMarker.getNegationRule().accept(this);
            }

            terminalRule.setMatchbufferPosition(_ruleNumber);
            _ruleNumber++;
        }
    }
}
