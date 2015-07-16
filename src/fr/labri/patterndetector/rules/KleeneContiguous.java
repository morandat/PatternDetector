package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.EventType;
import fr.labri.patterndetector.automaton.*;

/**
 * Created by william.braik on 10/07/2015.
 */

public class KleeneContiguous extends AbstractUnaryRule {

    public KleeneContiguous(Atom x) {
        super(RuleType.RULE_KLEENE, ".+", x);
    }

    @Override
    public IAutomaton buildAutomaton() {
        Atom x = (Atom) _r;

        IState s0 = new State(StateType.STATE_INITIAL, "0");
        IState s1 = new State(StateType.STATE_TAKE, "1");
        IState s2 = new State(StateType.STATE_IGNORE, "2");

        s0.registerTransition(x.getEventType(), s1);
        s1.registerTransition(x.getEventType(), s1);
        s1.registerTransition(EventType.EVENT_NEGATION, s2);

        IAutomaton automaton = new Automaton();
        automaton.setInitialState(s0);
        automaton.registerState(s1);
        automaton.registerFinalState(s2);

        return automaton;
    }
}