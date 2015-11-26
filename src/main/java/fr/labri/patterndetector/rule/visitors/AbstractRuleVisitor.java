package fr.labri.patterndetector.rule.visitors;

import fr.labri.patterndetector.rule.*;

/**
 * Created by wbraik on 22/10/15.
 * <p>
 * Default behavior for a rule visitor.
 */
public abstract class AbstractRuleVisitor implements IRuleVisitor {

    @Override
    public void visit(IRule rule) {
    }

    @Override
    public void visit(ICompositeRule compositeRule) {
        visit((IRule) compositeRule);
    }

    @Override
    public void visit(ITerminalRule terminalRule) {
        visit((IRule) terminalRule);
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


