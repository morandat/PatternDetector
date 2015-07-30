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
        left.getStates().values().forEach(automaton::registerState);
        IState q = left.getFinalState();
        q.setFinal(false);
        automaton.registerState(q);

        // right component
        // Merge p and q together (copy transitions of p and add them to q)
        IState p = right.getInitialState();
        right.getStates().values().forEach(automaton::registerState);
        p.getTransitions().values().forEach(t -> {
            try {
                q.registerTransition(t.getTarget(), t.getLabel(), t.getType());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
        automaton.registerFinalState(right.getFinalState());

        // add extra stuff to obtain the new automaton (Thompson's construction style)
        q.registerTransition(q, Transition.LABEL_NEGATION, TransitionType.TRANSITION_DROP);

        System.out.println("Final automaton : " + automaton);

        _automaton = automaton;
    }
}
