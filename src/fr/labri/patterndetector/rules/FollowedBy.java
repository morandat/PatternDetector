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
        if (q == null) {
            // Kleene automata don't have any final state.
            // TODO unsafe cast
            Kleene k = (Kleene) _left;
            q = left.getStateByLabel(k.getPivotStateLabel());
        } else {
            q.setFinal(false);
            automaton.registerState(q);
        }

        // right component
        // Merge p and q together (copy transitions of p and add them to q)
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

        if (right.getFinalState() != null) { // If the right component is a Kleene, then it is possible that there is no final state
            automaton.registerFinalState(right.getFinalState());
        }

        // add extra stuff to obtain the new automaton (Thompson's construction style)
        q.registerTransition(q, Transition.LABEL_NEGATION, TransitionType.TRANSITION_DROP);

        if (RuleType.RULE_KLEENE_CONTIGUOUS.equals(_left.getType())) {
            // If the left component is a Kleene Automaton
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
