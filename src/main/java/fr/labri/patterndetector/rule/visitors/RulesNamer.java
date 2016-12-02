package fr.labri.patterndetector.rule.visitors;

import fr.labri.patterndetector.rule.*;
import fr.labri.patterndetector.runtime.predicates.INacBeginMarker;

/**
 * Created by wbraik on 19/11/15.
 * <p>
 * Sets rule names
 * Sets rule positions
 */

public final class RulesNamer {

    static private int _ruleCounter = 0;

    private RulesNamer() {
    }

    public static int nameRules(IRule rule) {
        _ruleCounter = 0;
        RulesNamerVisitor visitor = new RulesNamerVisitor();
        rule.accept(visitor);

        int matchbufferSize = _ruleCounter;
        return matchbufferSize;
    }

    private static class RulesNamerVisitor extends AbstractRuleVisitor {
        @Override
        public void visit(AbstractUnaryRule unaryRule) {
            unaryRule.getChildRule().accept(new RulesNamerVisitor());
        }

        @Override
        public void visit(AbstractBinaryRule binaryRule) {
            binaryRule.getLeftChildRule().accept(new RulesNamerVisitor());
            binaryRule.getRightChildRule().accept(new RulesNamerVisitor());
        }

        @Override
        public void visit(AbstractTerminalRule terminalRule) {
            for (INacBeginMarker startNacMarker : terminalRule.getNacBeginMarkers()) {
                startNacMarker.getNacRule().accept(new RulesNamerVisitor());
            }

            terminalRule.setName("" + _ruleCounter);
            terminalRule.setMatchbufferPosition(_ruleCounter);
            _ruleCounter++;
        }
    }
}
