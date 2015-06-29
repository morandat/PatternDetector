package fr.labri.streamchecking.rules;

import fr.labri.streamchecking.EventType;
import fr.labri.streamchecking.automaton.*;

/**
 * Created by William Braik on 6/25/2015.
 */
public class AlwaysFollowedBy extends AbstractBinaryRule {

    public AlwaysFollowedBy(IRule left, IRule right) {
        super(RuleType.RULE_ALWAYS_FOLLOWED_BY, "-->", left, right);
    }

    @Override
    public IAutomaton buildAutomaton() {
        IState s0 = new State(StateType.STATE_INITIAL, "0");
        IState s1 = new State(StateType.STATE_NORMAL, "1");
        IState s2 = new State(StateType.STATE_FINAL, "2");

        s0.registerTransition(EventType.EVENT_A, s1);
        s1.registerTransition(EventType.EVENT_A, s1); //TODO Should be !b
        s1.registerTransition(EventType.EVENT_C, s1);
        s1.registerTransition(EventType.EVENT_B, s2);

        // a --> b
        IAutomaton a = new Automaton();
        a.setInitialState(s0);
        a.registerState(s1);
        a.registerState(s2);

        return a;
    }
}
