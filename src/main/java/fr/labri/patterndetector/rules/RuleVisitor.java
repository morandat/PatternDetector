package fr.labri.patterndetector.rules;

/**
 * Created by wbraik on 22/10/15.
 * <p>
 * Default behavior for a rule visitor.
 */
public abstract class RuleVisitor {

    public void startVisit(IRule root) {
        root.accept(this);
    }

    public void visit(IRule rule) {
        rule.accept(this);
    }

    public void visit(ITerminalRule terminalRule) {
        visit((IRule) terminalRule);
    }

    public void visit(ICompositeRule compositeRule) {
        compositeRule.getChildRules().forEach(rule -> visit((IRule) rule));
    }

    public void visit(IUnaryRule unaryRule) {
        visit((ICompositeRule) unaryRule);
    }

    public void visit(IBinaryRule binaryRule) {
        visit((ICompositeRule) binaryRule);
    }

    public void visit(IAtom atom) {
        visit((ITerminalRule) atom);
    }

    public void visit(Kleene kleene) {
        visit((ICompositeRule) kleene);
    }

    public void visit(FollowedBy followedBy) {
        visit((ICompositeRule) followedBy);
    }
}
