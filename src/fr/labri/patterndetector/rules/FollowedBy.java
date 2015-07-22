package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.EventType;
import fr.labri.patterndetector.automaton.*;

/**
 * Created by William Braik on 6/25/2015.
 */
public class FollowedBy extends AbstractBinaryRule {

    public FollowedBy(IRule left, IRule right) {
        super(RuleType.RULE_FOLLOWED_BY, "-->", left, right);

        try {
            buildAutomaton();
        } catch (Exception e) {
            System.err.println("Can't instantiate rule ! " + e.getMessage());
        }
    }

    public void buildAutomaton() throws Exception {
        if (_left instanceof Atom && _right instanceof Atom) {
            Atom left = (Atom) _left;
            Atom right = (Atom) _right;

            IState s0 = new State(false); // Initial state
            IState s1 = new State(true);
            IState s2 = new State(false);
            IState s3 = new State(true);

            s0.registerTransition(left.getEventType(), s1);
            s1.registerTransition(left.getEventType(), s1);
            s1.registerTransition(EventType.EVENT_NEGATION, s2);
            s2.registerTransition(left.getEventType(), s1);
            s2.registerTransition(right.getEventType(), s3);
            s2.registerTransition(EventType.EVENT_NEGATION, s2);
            s1.registerTransition(right.getEventType(), s3);

            IAutomaton automaton = new Automaton();
            automaton.registerInitialState(s0);
            automaton.registerState(s1);
            automaton.registerState(s2);
            automaton.registerFinalState(s3);

            _automaton = automaton;
        }
    }
}
