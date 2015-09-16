package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by william.braik on 08/07/2015.
 */
public class AtomNot extends AbstractRule implements IAtom {

    protected String _x; // This atom represents all event types except x
    protected Map<String, Predicate<Integer>> _predicates;


    public AtomNot(String x) {
        super(RuleType.RULE_ATOM_NOT, null);
        _x = x;
        _predicates = new HashMap<>();
    }

    @Override
    public String getEventType() {
        return _x;
    }

    @Override
    public IAtom setPredicateOnField(String field, Predicate<Integer> predicate) {
        _predicates.put(field, predicate);

        return this;
    }

    @Override
    public String toString() {
        return "!" + _x;
    }

    public void buildAutomaton() throws Exception {
        IState s0 = new State(); // Initial state
        IState s1 = new State(); // Final state

        s0.registerTransition(s0, _x, TransitionType.TRANSITION_DROP);
        s0.registerTransition(s1, Transition.LABEL_NEGATION, TransitionType.TRANSITION_APPEND, _predicates);

        IRuleAutomaton automaton = new RuleAutomaton(this);
        automaton.registerInitialState(s0);
        automaton.registerFinalState(s1);

        _automaton = automaton;
    }
}
