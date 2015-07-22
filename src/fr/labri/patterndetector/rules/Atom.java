package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.EventType;
import fr.labri.patterndetector.automaton.*;

/**
 * Created by William Braik on 6/27/2015.
 */
public class Atom extends AbstractRule implements IAtom {

    protected EventType _x;

    public Atom(EventType x) {
        super(RuleType.RULE_ATOM, null);
        _x = x;

        try {
            buildAutomaton();
        } catch (Exception e) {
            System.err.println("Can't instantiate rule ! " + e.getMessage());
        }
    }

    @Override
    public EventType getEventType() {
        return _x;
    }

    @Override
    public String toString() {
        return _x.toString();
    }

    public void buildAutomaton() throws Exception {
        IState s0 = new State(false); // Initial state
        IState s1 = new State(true);

        s0.registerTransition(_x, s1);

        IAutomaton automaton = new Automaton();
        automaton.registerInitialState(s0);
        automaton.registerFinalState(s1);

        _automaton = automaton;
    }
}
