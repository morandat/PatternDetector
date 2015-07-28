package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

/**
 * Created by William Braik on 6/27/2015.
 */
public class Atom extends AbstractRule implements IAtom {

    protected String _x;

    public Atom(String x) {
        super(RuleType.RULE_ATOM, null);
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
        return _x;
    }

    public void buildAutomaton() throws Exception {
        IState s0 = new State(); // Initial state
        IState s1 = new State();

        s0.registerTransition(s1, _x, TransitionType.TRANSITION_APPEND);

        IAutomaton automaton = new Automaton();
        automaton.registerInitialState(s0);
        automaton.registerFinalState(s1);

        _automaton = automaton;
    }
}
