package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.rule.visitors.IRuleVisitor;

import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by William Braik on 6/27/2015.
 * <p>
 * The atom is the most elementary rule.
 * It represents the occurrence of a given event type.
 * Atoms can be used within FollowedBy and Kleene rules, to describe more complex rules.
 */
public class Atom extends AbstractAtom {

    public Atom(String eventType) {
        super("", eventType);
    } // FIXME ""

    @Override
    public void accept(IRuleVisitor visitor) {
        visitor.visit(this);
    }
}
