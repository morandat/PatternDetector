package fr.labri.patterndetector.rule.visitors;

import fr.labri.patterndetector.rule.Atom;
import fr.labri.patterndetector.rule.AtomNot;
import fr.labri.patterndetector.rule.FollowedBy;
import fr.labri.patterndetector.rule.Kleene;

/**
 * Created by wbraik on 19/11/15.
 * <p>
 * Classic depth first traversal
 */

public class DefaultTraversal extends AbstractRuleVisitor {

    public void visit(Atom atom) {
    }

    public void visit(AtomNot atomNot) {
    }

    public void visit(Kleene kleene) {
        kleene.getChildRule().accept(this);
    }

    public void visit(FollowedBy followedBy) {
        followedBy.getLeftChildRule().accept(this);
        followedBy.getRightChildRule().accept(this);
    }
}
