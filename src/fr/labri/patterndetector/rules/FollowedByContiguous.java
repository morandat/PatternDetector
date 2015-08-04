package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

import java.util.Collection;

/**
 * Created by William Braik on 6/25/2015.
 */
public class FollowedByContiguous extends AbstractBinaryRule {

    public FollowedByContiguous(IRule left, IRule right) {
        super(RuleType.RULE_FOLLOWED_BY_CONTIGUOUS, ".", left, right);

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
        if (q == null) {
            // Kleene automata don't have any final state.
            Kleene k = (Kleene) _left;
            q = left.getStateByLabel(k.getPivotStateLabel());
        } else {
            q.setFinal(false);
            automaton.registerState(q);
        }

        // right component
        IState p = right.getInitialState();
        right.getStates().values().forEach(automaton::registerState);
        final IState qFinal = q;
        p.getTransitions().forEach(t -> {
            try {
                qFinal.registerTransition(t.getTarget(), t.getLabel(), t.getType());
            } catch (Exception e) {
                System.err.println("An error occurred while building the automaton (" + e.getMessage() + ")");
            }
        });
        automaton.registerFinalState(right.getFinalState());

        // add extra stuff to obtain the new automaton (Thompson's construction style)

        // If the left component is NOT a Kleene Automaton
        // TODO this is a bit too complex... see if there is a simpler way
        if (!RuleType.RULE_KLEENE_CONTIGUOUS.equals(_left.getType()) && !RuleType.RULE_KLEENE.equals(_left.getType())) {
        /* For each non-initial and non-final state, if there aren't any outgoing Epsilon or Star transitions,
        outgoing transitions based on those of the initial state (same target, same label, same type) must be added to it.
        The type of those transitions has to be changed to OVERWRITE only if they are non-looping. */
            Collection<ITransition> transitionsFromInitial = automaton.getInitialState().getTransitions();
            automaton.getStates().values().forEach(state -> {
                try {
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
                                System.err.println("An error occurred while building the automaton (" + e.getMessage() + ")");
                                e.printStackTrace();
                            }
                        });
                    }
                } catch (Exception e) {
                    System.err.println("An error occurred while building the automaton (" + e.getMessage() + ")");
                    e.printStackTrace();
                }
            });
        } else if (RuleType.RULE_KLEENE_CONTIGUOUS.equals(_left.getType())) {
            // If the left component is a Contiguous Kleene Automaton
            boolean ok = false;
            for (IState s : left.getStates().values()) {
                for (ITransition t : s.getTransitions()) {
                    if (t.getTarget().equals(left.getFinalState())) {
                        s.removeTransition(t.getLabel());
                        s.registerTransition(t.getTarget(), Transition.LABEL_EPSILON, TransitionType.TRANSITION_DROP);
                        ok = true;
                        break;
                    }
                }
                if (ok) break;
            }
        }

        System.out.println("Final automaton : " + automaton);

        _automaton = automaton;
    }
}
