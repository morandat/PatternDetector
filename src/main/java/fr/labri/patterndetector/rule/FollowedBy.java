package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.rule.visitors.IRuleVisitor;

/**
 * Created by William Braik on 6/25/2015.
 * <p>
 * The FollowedBy rule captures the occurrence of a rule (left operand), followed by another rule (right operand).
 * TODO The selection policy (first, each, or last) determines which of the captured events on the left side are selected.
 */
public class FollowedBy extends AbstractBinaryRule {

    public static final String Symbol = "";

    public FollowedBy(IRule left, IRule right) {
        super(FollowedBy.Symbol, left, right);
    }

    // TODO the following constructors should not exist. This code should be in a BinaryRuleFactory : Binary.Or("a","b")
    public FollowedBy(String e, IRule right) {
        super(FollowedBy.Symbol,
                new Atom(e),
                right);
    }

    public FollowedBy(IRule left, String e) {
        super(FollowedBy.Symbol,
                left,
                new Atom(e));
    }

    public FollowedBy(String e1, String e2) {
        super(FollowedBy.Symbol,
                new Atom(e1),
                new Atom(e2));
    }

    @Override
    public void accept(IRuleVisitor visitor) {
        visitor.visit(this);
    }
}
