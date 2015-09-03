package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by William Braik on 6/27/2015.
 */
public class Atom extends AbstractRule implements IAtom {

    protected String _x;
    protected Map<String, Predicate<Integer>> _predicates;

    public Atom(String x) {
        super(RuleType.RULE_ATOM, null);
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
        return _x;
    }

    @Override
    public void buildAutomaton() throws Exception {
        IState s0 = new State(); // Initial state
        IState s1 = new State(); // Final state

        s0.registerTransition(s1, _x, TransitionType.TRANSITION_APPEND, _predicates);

        IRuleAutomaton automaton = new RuleAutomaton(this);
        automaton.registerInitialState(s0);
        automaton.registerFinalState(s1);

        _automaton = automaton;
    }
}
