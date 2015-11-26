package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.rule.visitors.IRuleVisitor;

import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by william.braik on 08/07/2015.
 * <p>
 * The complement of an atom.
 * Given an event type x, it represents an event of any type besides x.
 */
@Deprecated //TODO to reintroduce once the ImmediatelyFollowedBy operator comes back
public class AtomNot extends AbstractAtom {

    public AtomNot(String eventType) {
        super("!", eventType);
    }

    public AtomNot(String eventType, Map<String, Predicate<Integer>> predicates) {
        super(eventType, predicates);
    }

    @Override
    public void accept(IRuleVisitor visitor) {
        visitor.visit(this);
    }
}
