package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;
import fr.labri.patterndetector.automaton.exception.RuleAutomatonException;
import fr.labri.patterndetector.compiler.RuleVisitor;

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

    public void buildAutomaton() throws RuleAutomatonException {
        IState i = new State(); // Initial state
        IState f = new State(); // Final state

        i.registerTransition(i, _eventType, TransitionType.TRANSITION_DROP);
        i.registerTransition(f, Transition.LABEL_STAR, TransitionType.TRANSITION_APPEND, _predicates);

        IRuleAutomaton automaton = new RuleAutomaton();
        automaton.setInitialState(i);
        automaton.setFinalState(f);
        _connectionStateLabel = f.getLabel();

        _automaton = automaton;
    }

    @Override
    public String toString() {
        return "!" + _eventType;
    }

    @Override
    public void accept(RuleVisitor visitor) {
        visitor.visit(this);
    }
}
