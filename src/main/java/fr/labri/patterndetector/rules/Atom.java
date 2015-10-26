package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by William Braik on 6/27/2015.
 * <p>
 * The atom is the most elementary rule.
 * It represents the occurrence of a given event type.
 * Atoms can be used within FollowedBy and Kleene rules, to specify more complex rules.
 */
public class Atom extends AbstractRule implements IAtom {

    protected String _eventType; // The event type
    protected Map<String, Predicate<Integer>> _predicates; // Maps fields of the event's payload to predicates TODO à mettre au niveau règle ?

    public Atom(String eventType) {
        super(null);
        _eventType = eventType;
        _predicates = new HashMap<>();
    }

    @Override
    public String getEventType() {
        return _eventType;
    }

    @Override
    public IAtom setPredicate(String field, Predicate<Integer> predicate) {
        _predicates.put(field, predicate);

        return this;
    }

    @Override
    public String toString() {
        return _eventType;
    }

    @Override
    public void buildAutomaton() throws Exception {
        IState i = new State(); // Initial state
        IState f = new State(); // Final state

        i.registerTransition(f, _eventType, TransitionType.TRANSITION_APPEND, _predicates);

        IRuleAutomaton automaton = new RuleAutomaton(this);
        automaton.setInitialState(i);
        automaton.setFinalState(f);
        _connectionStateLabel = f.getLabel();

        _automaton = automaton;
    }

    @Override
    public void accept(RuleVisitor visitor) {
        visitor.visit(this);
    }
}
