package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

/**
 * Created by william.braik on 08/07/2015.
 */
public class AtomNot extends AbstractRule implements IAtom {

    protected String _x; // This atom represents all event types except this one (!x)

    public AtomNot(String x) {
        super(RuleType.RULE_ATOM_NOT, null);
        _x = x;

        try {
            buildAutomaton();
        } catch (Exception e) {
            System.err.println("Can't instantiate rule ! " + e.getMessage());
        }
    }

    @Override
    public String getEventType() {
        return _x;
    }

    @Override
    public String toString() {
        return "!" + _x;
    }

    public void buildAutomaton() throws Exception {
        IState s0 = new State(); // Initial state
        IState s1 = new State();

        s0.registerTransition(s0, _x, TransitionType.TRANSITION_DROP);
        s0.registerTransition(s1, Transition.LABEL_NEGATION, TransitionType.TRANSITION_APPEND);

        IRuleAutomaton automaton = new RuleAutomaton(this);
        automaton.registerInitialState(s0);
        automaton.registerFinalState(s1);

        _automaton = automaton;
    }
}
