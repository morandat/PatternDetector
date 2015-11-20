package fr.labri.patterndetector.compiler;

import fr.labri.patterndetector.rules.*;

/**
 * Created by wbraik on 22/10/15.
 * <p>
 * Default behavior for a rule visitor.
 */
public abstract class RuleVisitor implements IRuleVisitor {

    @Override
    public void visit(IRule rule) {
        rule.accept(this);
    }

    @Override
    public void visit(ITerminalRule terminalRule) {
        visit((IRule) terminalRule);
    }

    @Override
    public void visit(ICompositeRule compositeRule) {
        compositeRule.getChildRules().forEach(rule -> visit((IRule) rule));
    }

    @Override
    public void visit(IUnaryRule unaryRule) {
        visit((ICompositeRule) unaryRule);
    }

    @Override
    public void visit(IBinaryRule binaryRule) {
        visit((ICompositeRule) binaryRule);
    }

    @Override
    public void visit(IAtom atom) {
        visit((ITerminalRule) atom);
    }

    @Override
    public void visit(Atom atom) {
        visit((ITerminalRule) atom);
    }

    @Override
    public void visit(Kleene kleene) {
        visit((ICompositeRule) kleene);
    }

    @Override
    public void visit(FollowedBy followedBy) {
        visit((ICompositeRule) followedBy);
    }
}


