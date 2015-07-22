package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.EventType;
import fr.labri.patterndetector.automaton.Automaton;
import fr.labri.patterndetector.automaton.IAutomaton;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.automaton.State;

/**
 * Created by william.braik on 08/07/2015.
 */
public class AtomNot extends AbstractRule implements IAtom {

    protected EventType _x; // This atom represents all event types except this one (!x)

    public AtomNot(EventType x) {
        super(RuleType.RULE_ATOM_NOT, null);
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
        return "!" + _x.toString();
    }

    public void buildAutomaton() throws Exception {
        IState s0 = new State(false); // Initial state
        IState s1 = new State(true);

        s0.registerTransition(_x, s0);
        s0.registerTransition(EventType.EVENT_NEGATION, s1);

        IAutomaton automaton = new Automaton();
        automaton.registerInitialState(s0);
        automaton.registerFinalState(s1);

        _automaton = automaton;
    }
}
