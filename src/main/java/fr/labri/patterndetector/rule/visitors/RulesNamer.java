package fr.labri.patterndetector.rule.visitors;

import fr.labri.patterndetector.rule.*;
import fr.labri.patterndetector.runtime.expressions.INacBeginMarker;

/**
 * Created by wbraik on 19/11/15.
 * <p>
 * Sets rule names
 * Sets rule positions
 */

public final class RulesNamer {

    public static int numberRule(IRule rule) {
        RulesNamerVisitor visitor = new RulesNamerVisitor();
        rule.accept(visitor);
        return visitor._ruleCounter;
    }

    private static class RulesNamerVisitor extends AbstractRuleVisitor {
        private int _ruleCounter = 0;
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
            for (INacBeginMarker startNacMarker : terminalRule.getNacBeginMarkers()) {
                startNacMarker.getNacRule().accept(this);
            }

            terminalRule.setName("" + _ruleCounter);
            terminalRule.setMatchbufferPosition(_ruleCounter);
            _ruleCounter++;
        }
    }
}
