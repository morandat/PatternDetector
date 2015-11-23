package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;
import fr.labri.patterndetector.automaton.exception.RuleAutomatonException;
import fr.labri.patterndetector.compiler.IRuleVisitor;

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
        super(eventType);
    }

    public Atom(String eventType, Map<String, Predicate<Integer>> predicates) {
        super(eventType, predicates);
    }

    @Override
    public String toString() {
        return _eventType;
    }

    @Override
    public void accept(IRuleVisitor visitor) {
        visitor.visit(this);
    }
}
