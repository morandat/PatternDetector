package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by William Braik on 6/27/2015.
 * <p>
 * The elementary component of a pattern, which represents an event of a given type.
 */
public class Atom extends AbstractRule implements IAtom {

    protected String _eventType;
    protected Map<String, Predicate<Integer>> _predicates; // Maps fields of the event's payload to predicates

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
    public IAtom setPredicateOnField(String field, Predicate<Integer> predicate) {
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
        automaton.registerInitialState(i);
        automaton.registerFinalState(f);
        _connectionStateLabel = f.getLabel();

        _automaton = automaton;
    }
}
