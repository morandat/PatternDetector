package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

/**
 * Created by William Braik on 6/25/2015.
 */
public class FollowedBy extends AbstractBinaryRule {

    public static final String Symbol = "-->";

    public FollowedBy(IRule left, IRule right) {
        super(RuleType.RULE_FOLLOWED_BY, FollowedBy.Symbol, left, right);
    }

    public FollowedBy(String e, IRule right) {
        super(RuleType.RULE_FOLLOWED_BY, FollowedBy.Symbol, new Atom(e), right);
    }

    public FollowedBy(IRule left, String e) {
        super(RuleType.RULE_FOLLOWED_BY, FollowedBy.Symbol, left, new Atom(e));
    }

    public FollowedBy(String e1, String e2) {
        super(RuleType.RULE_FOLLOWED_BY, FollowedBy.Symbol, new Atom(e1), new Atom(e2));
    }

    @Override
    public void buildAutomaton() throws Exception {
        IRuleAutomaton left = AutomatonUtils.copy(_left.getAutomaton());
        IRuleAutomaton right = AutomatonUtils.copy(_right.getAutomaton());
        IRuleAutomaton automaton = new RuleAutomaton(this);

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

        if (right.getFinalState() != null) { // If the right component is Kleene, then the automaton has no final state
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

        _automaton = automaton;

        createClockConstraints(q);
    }

    /**
     * // If there is a time constraint specified on the rule, create corresponding clock constraints
     *
     * @param q The original final state of the left automaton of the left rule
     */
    private void createClockConstraints(IState q) {
        if (_timeConstraint != null) {
            int value = _timeConstraint.getValue();

            q.getTransitions().forEach(t -> t.setClockConstraint(RuleUtils.getRightmostAtom(_left).getEventType(),
                    value));
        }
    }
}
