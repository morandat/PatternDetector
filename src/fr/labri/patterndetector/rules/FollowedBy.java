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
            System.err.println("Can't instantiate rule ! (" + e.getMessage() + ")");
        }
    }

    public void buildAutomaton() throws Exception {
        IAutomaton left = Automata.copy(_left.getAutomaton());
        IAutomaton right = Automata.copy(_right.getAutomaton());

        System.out.println(left);
        System.out.println(right);

        IAutomaton automaton = new Automaton();

        // left component
        automaton.registerInitialState(left.getInitialState());
        for (IState s : left.getStates()) {
            automaton.registerState(s);
        }
        IState q = left.getFinalState();
        q.setFinal(false);
        automaton.registerState(q);

        // right component
        IState p = right.getInitialState();
        p.setInitial(false);
        automaton.registerState(p);
        for (IState s : right.getStates()) {
            automaton.registerState(s);
        }
        automaton.registerFinalState(right.getFinalState());

        // extra states and transitions
        IState r = new State(false);
        automaton.registerState(r);
        q.registerTransition(EventType.EVENT_EPSILON, p);
        q.registerTransition(EventType.EVENT_NEGATION, r);
        r.registerTransition(EventType.EVENT_NEGATION, r);
        r.registerTransition(EventType.EVENT_EPSILON, p);
        p.registerTransition(EventType.EVENT_NEGATION, r);

        System.out.println(automaton);

        _automaton = automaton;
    }
}
