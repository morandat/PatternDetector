package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;
import fr.labri.patterndetector.automaton.exception.RuleAutomatonException;
import fr.labri.patterndetector.compiler.IRuleVisitor;

import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by william.braik on 08/07/2015.
 * <p>
 * The complement of an atom.
 * Given an event type x, it represents an event of any type besides x.
 */
@Deprecated
public class AtomNot extends AbstractAtom {

    public AtomNot(String eventType) {
        super(eventType);
    }

    public AtomNot(String eventType, Map<String, Predicate<Integer>> predicates) {
        super(eventType, predicates);
    }

    @Override
    public String toString() {
        return "!" + _eventType;
    }

    @Override
    public void accept(IRuleVisitor visitor) {
        visitor.visit(this);
    }
}
