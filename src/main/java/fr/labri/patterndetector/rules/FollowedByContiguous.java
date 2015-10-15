package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

import java.util.Collection;

/**
 * Created by William Braik on 6/25/2015.
 */
public class FollowedByContiguous extends AbstractBinaryRule {

    public static final String Symbol = ".";

    public FollowedByContiguous(IRule left, IRule right) {
        super(RuleType.RULE_FOLLOWED_BY_CONTIGUOUS, FollowedByContiguous.Symbol, left, right);
    }

    public FollowedByContiguous(String e, IRule right) {
        super(RuleType.RULE_FOLLOWED_BY_CONTIGUOUS, FollowedByContiguous.Symbol,
                (e.startsWith("!") ? new AtomNot(e) : new Atom(e)),
                right);
    }

    public FollowedByContiguous(IRule left, String e) {
        super(RuleType.RULE_FOLLOWED_BY_CONTIGUOUS, FollowedByContiguous.Symbol,
                left,
                (e.startsWith("!") ? new AtomNot(e) : new Atom(e)));
    }

    public FollowedByContiguous(String e1, String e2) {
        super(RuleType.RULE_FOLLOWED_BY_CONTIGUOUS, FollowedByContiguous.Symbol,
                (e1.startsWith("!") ? new AtomNot(e1) : new Atom(e1)),
                (e2.startsWith("!") ? new AtomNot(e2) : new Atom(e2)));
    }

    @Override
    public void buildAutomaton() throws Exception {
        IRuleAutomaton left = AutomatonUtils.copy(_left.getAutomaton());
        IRuleAutomaton right = AutomatonUtils.copy(_right.getAutomaton());

        IRuleAutomaton automaton = new RuleAutomaton(this);

        // Left component
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

        // Right component
        // Merge p and q together (copy transitions of p and add them to q)
        IState p = right.getInitialState();
        right.getStates().values().forEach(automaton::registerState);
        final IState qFinal = q;
        p.getTransitions().forEach(t -> {
            try {
                if (t.getTarget().equals(p)) {
                    qFinal.registerTransition(qFinal, t.getLabel(), t.getType());
                } else {
                    qFinal.registerTransition(t.getTarget(), t.getLabel(), t.getType());
                }
            } catch (Exception e) {
                System.err.println("An error occurred while building the automaton (" + e.getMessage() + ")");
            }
        });

        if (right.getFinalState() != null) { // If the right component is Kleene, then the automaton has no final state
            automaton.registerFinalState(right.getFinalState());
        }

        // Add extra stuff to obtain the new automaton (Thompson's construction style)

        // If the left component is NOT a Kleene automaton (contiguous or not)
        // TODO this part is a bit too complex...
        if (!RuleType.RULE_KLEENE_CONTIGUOUS.equals(_left.getType()) && !RuleType.RULE_KLEENE.equals(_left.getType())) {
        /* For each non-initial and non-final state, if there aren't any outgoing Epsilon or Star transitions,
        outgoing transitions of the initial state (same target, same label, same type) must be added to it.
        The type of those transitions has to be changed to OVERWRITE only if they are non-looping. */
            Collection<ITransition> transitionsFromInitial = automaton.getInitialState().getTransitions();
            automaton.getStates().values().forEach(state -> {
                try {
                    if (state.getTransitionByLabel(Transition.LABEL_EPSILON) == null
                            && state.getTransitionByLabel(Transition.LABEL_NEGATION) == null) {
                        transitionsFromInitial.forEach(t -> {
                            try {
                                if (state.getTransitionByLabel(t.getLabel()) == null) {
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
            // If the left component IS a Contiguous Kleene Automaton, "discard" the final state by making it reachable via epsilon only
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

        createClockConstraints(q, right);
    }

    /**
     * // If there is a time constraint specified on the rule, create corresponding clock constraints
     *
     * @param q     The original final state of the left automaton of the left rule
     * @param right The automaton of the right rule
     */
    private void createClockConstraints(IState q, IRuleAutomaton right) {
        if (_timeConstraint != null) {
            int value = _timeConstraint.getValue();

            q.getTransitions().forEach(t -> {
                // An overwrite type of transition is supposed to reset the word so it shouldn't have a clock guard
                if (!TransitionType.TRANSITION_OVERWRITE.equals(t.getType())) {
                    t.setClockConstraint(RuleUtils.getRightmostAtom(_left).getEventType(), value);
                }
            });

            if (_timeConstraint.isTransitive()) {
                right.getTransitions().forEach(t -> t.setClockConstraint(RuleUtils.getRightmostAtom(_left).getEventType(), value));
            }
        }
    }
}
