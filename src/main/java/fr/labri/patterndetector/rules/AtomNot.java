package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by william.braik on 08/07/2015.
 * <p>
 * An atom that represents an event of any type except a give type.
 */
public class AtomNot extends AbstractRule implements IAtom {

    protected String _eventType; // The atom represents an event of any type except this type;
    protected Map<String, Predicate<Integer>> _predicates; // Maps fields of the event's payload to predicates


    public AtomNot(String eventType) {
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
        return "!" + _eventType;
    }

    public void buildAutomaton() throws Exception {
        IState i = new State(); // Initial state
        IState f = new State(); // Final state

        i.registerTransition(i, _eventType, TransitionType.TRANSITION_DROP);
        i.registerTransition(f, Transition.LABEL_STAR, TransitionType.TRANSITION_APPEND, _predicates);

        IRuleAutomaton automaton = new RuleAutomaton(this);
        automaton.registerInitialState(i);
        automaton.registerFinalState(f);
        _connectionStateLabel = f.getLabel();

        _automaton = automaton;
    }
}
