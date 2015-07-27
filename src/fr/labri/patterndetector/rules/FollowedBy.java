package fr.labri.patterndetector.rules;

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
        IAutomaton left = AutomatonUtils.copy(_left.getAutomaton());
        IAutomaton right = AutomatonUtils.copy(_right.getAutomaton());

        System.out.println("Left component : " + left);
        System.out.println("Right component : " + right);

        IAutomaton automaton = new Automaton();

        // left component
        automaton.registerInitialState(left.getInitialState());
        for (IState s : left.getStates().values()) {
            automaton.registerState(s);
        }
        IState q = left.getFinalState();
        q.setFinal(false);
        automaton.registerState(q);

        // right component
        IState p = right.getInitialState();
        p.setInitial(false);
        automaton.registerState(p);
        for (IState s : right.getStates().values()) {
            automaton.registerState(s);
        }
        automaton.registerFinalState(right.getFinalState());

        // add extra stuff to obtain the final automaton (Thompson's construction style)
        q.registerTransition(p, AutomatonUtils.epsilonTransitionLabel(), false);
        p.registerTransition(p, AutomatonUtils.negativeTransitionLabel(), false);

        System.out.println("Final automaton : " + automaton);

        _automaton = automaton;
    }
}
