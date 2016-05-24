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
    public void visit(AbstractCompositeRule compositeRule) {
        visit((IRule) compositeRule);
    }

    @Override
    public void visit(AbstractTerminalRule terminalRule) {
        visit((IRule) terminalRule);
    }

    @Override
    public void visit(AbstractUnaryRule unaryRule) {
        visit((AbstractCompositeRule) unaryRule);
    }

    @Override
    public void visit(AbstractBinaryRule binaryRule) {
        visit((AbstractCompositeRule) binaryRule);
    }

    @Override
    public void visit(AbstractAtom atom) {
        visit((AbstractTerminalRule) atom);
    }

    @Override
    public void visit(Atom atom) {
        visit((AbstractAtom) atom);
    }

    @Override
    public void visit(Kleene kleene) {
        visit((AbstractKleene) kleene);
    }

    @Override
    public void visit(FollowedBy followedBy) {
        visit((AbstractBinaryRule) followedBy);
    }

    @Override
    public void visit(Or or) {
        visit((AbstractBinaryRule) or);
    }
}


