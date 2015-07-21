package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.EventType;
import fr.labri.patterndetector.automaton.*;

/**
 * Created by william.braik on 10/07/2015.
 */

public class KleeneContiguous extends AbstractUnaryRule {

    public KleeneContiguous(Atom x) {
        super(RuleType.RULE_KLEENE, ".+", x);

        try {
            buildAutomaton();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void buildAutomaton() throws Exception {
        Atom x = (Atom) _r;

        IState s0 = new State(false);
        IState s1 = new State(true);
        IState s2 = new State(false);

        s0.registerTransition(x.getEventType(), s1);
        s1.registerTransition(x.getEventType(), s1);
        s1.registerTransition(EventType.EVENT_NEGATION, s2);

        IAutomaton automaton = new Automaton();
        automaton.registerInitialState(s0);
        automaton.registerState(s1);
        automaton.registerFinalState(s2);

        _automaton = automaton;
    }
}