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

        // add extra stuff to obtain the new automaton (Thompson's construction style)
        q.registerTransition(p, Transition.LABEL_EPSILON, TransitionType.TRANSITION_DROP);

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
