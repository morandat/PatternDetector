package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

import java.util.Collection;

/**
 * Created by William Braik on 6/25/2015.
 */
public class ImmediatelyFollowedBy extends AbstractBinaryRule {

    public ImmediatelyFollowedBy(IRule left, IRule right) {
        super(RuleType.RULE_FOLLOWED_BY, ".", left, right);

        try {
            buildAutomaton();
        } catch (Exception e) {
            System.err.println(e.getMessage());
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
        /* For each non-initial and non-final state, if it doesn't have any outgoing Epsilon or Star transitions,
        add outgoing transitions that are identical to the ones of the initial state. The type of these transitions
        has to be OVERWRITE when their target isn't the initial state. */
        Collection<ITransition> transitionsFromInitial = automaton.getInitialState().getTransitions().values();
        automaton.getStates().values().forEach(state -> {
            if (state.getTransitionByLabel(Transition.LABEL_EPSILON) == null
                    && state.getTransitionByLabel(Transition.LABEL_NEGATION) == null) {
                transitionsFromInitial.forEach(t -> {
                    try {
                        if (t.getTarget().getLabel().equals(t.getSource().getLabel())) {
                            state.registerTransition(t.getTarget(), t.getLabel(), t.getType());
                        } else {
                            state.registerTransition(t.getTarget(), t.getLabel(), TransitionType.TRANSITION_OVERWRITE);
                        }
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                });
            }
        });

        System.out.println("Final automaton : " + automaton);

        _automaton = automaton;
    }
}
