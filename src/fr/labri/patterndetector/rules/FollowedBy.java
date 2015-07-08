package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.EventType;
import fr.labri.patterndetector.automaton.*;

/**
 * Created by William Braik on 6/25/2015.
 */
public class FollowedBy extends AbstractBinaryRule {

    public FollowedBy(IRule left, IRule right) {
        super(RuleType.RULE_FOLLOWED_BY, "-->", left, right);
    }

    public FollowedBy(IRule left, IRule right, TimeConstraint tc) {
        super(RuleType.RULE_FOLLOWED_BY, "-->", left, right, tc);
    }

    @Override
    public IAutomaton buildAutomaton() {
        IState s0 = new State(StateType.STATE_INITIAL, "0");
        IState s1 = new State(StateType.STATE_NORMAL, "1");
        IState s2 = new State(StateType.STATE_FINAL, "2");

        s0.registerTransition(EventType.EVENT_A, s1);
        s1.registerTransition(EventType.EVENT_SPECIAL, s1); // !b
        s1.registerTransition(EventType.EVENT_B, s2);

        // a --> b
        IAutomaton automaton = new Automaton();
        automaton.setInitialState(s0);
        automaton.registerState(s1);
        automaton.registerState(s2);

        return automaton;
    }
}
