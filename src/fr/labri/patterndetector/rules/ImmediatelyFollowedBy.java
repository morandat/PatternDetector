package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.EventType;
import fr.labri.patterndetector.automaton.*;

/**
 * Created by William Braik on 6/25/2015.
 */
public class ImmediatelyFollowedBy extends AbstractBinaryRule {

    public ImmediatelyFollowedBy(IRule left, IRule right) {
        super(RuleType.RULE_FOLLOWED_BY, ".", left, right);
    }

    @Override
    public IAutomaton buildAutomaton() throws Exception {
        if (_left instanceof Atom && _right instanceof Atom) {

            Atom left = (Atom) _left; // TODO atom for tests, must be rules though
            Atom right = (Atom) _right;

            IState s0 = new State(StateType.STATE_INITIAL, "0");
            IState s1 = new State(StateType.STATE_TAKE, "1");
            IState s2 = new State(StateType.STATE_TAKE, "2");
            IState s3 = new State(StateType.STATE_IGNORE, "3");

            s0.registerTransition(left.getEventType(), s1);
            s1.registerTransition(right.getEventType(), s2);
            s1.registerTransition(EventType.EVENT_NEGATION, s3);

            IAutomaton automaton = new Automaton();
            automaton.setInitialState(s0);
            automaton.registerState(s1);
            automaton.registerFinalState(s2);
            automaton.registerFinalState(s3);

            return automaton;
        } else {
            throw new Exception("This operator currently does not support rule composition");
        }
    }
}
