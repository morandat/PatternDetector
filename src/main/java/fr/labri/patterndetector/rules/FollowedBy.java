package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

import java.util.HashSet;

/**
 * Created by William Braik on 6/25/2015.
 */
public class FollowedBy extends AbstractBinaryRule implements INotContiguous {

    public static final String Symbol = "-->";

    public FollowedBy(IRule left, IRule right) {
        super(RuleType.RULE_FOLLOWED_BY, FollowedBy.Symbol, left, right);
    }

    public FollowedBy(String e, IRule right) {
        super(RuleType.RULE_FOLLOWED_BY, FollowedBy.Symbol,
                (e.startsWith("!") ? new AtomNot(e.substring(1)) : new Atom(e)),
                right);
    }

    public FollowedBy(IRule left, String e) {
        super(RuleType.RULE_FOLLOWED_BY, FollowedBy.Symbol,
                left,
                (e.startsWith("!") ? new AtomNot(e.substring(1)) : new Atom(e)));
    }

    public FollowedBy(String e1, String e2) {
        super(RuleType.RULE_FOLLOWED_BY, FollowedBy.Symbol,
                (e1.startsWith("!") ? new AtomNot(e1.substring(1)) : new Atom(e1)),
                (e2.startsWith("!") ? new AtomNot(e2.substring(1)) : new Atom(e2)));
    }

    @Override
    public void addRuleNegation(IRule rule) {
        if (_negationRules == null)
            _negationRules = new HashSet<>();

        _negationRules.add(rule);
    }

    @Override
    public void buildAutomaton() throws Exception {
        IRuleAutomaton left = AutomatonUtils.copy(_left.getAutomaton());
        IRuleAutomaton right = AutomatonUtils.copy(_right.getAutomaton());
        IRuleAutomaton automaton = new RuleAutomaton(this, _negationRules);

        // Left component
        automaton.registerInitialState(left.getInitialState());
        left.getStates().values().forEach(automaton::registerState);
        if (left.getResetState() != null) {
            automaton.registerResetState(left.getResetState());
        }

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
        if (right.getResetState() != null) {
            automaton.registerResetState(right.getResetState());
        }

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
        if (q.getTransitionByLabel(Transition.LABEL_NEGATION) == null) { // this check is for AtomNot : its initial state already has a negative transition...
            q.registerTransition(q, Transition.LABEL_NEGATION, TransitionType.TRANSITION_DROP);
        }

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

        //System.out.println(left);
        //System.out.println(right);

        _automaton = automaton;

        createClockConstraints(q, right);
    }

    /**
     * // If there is a time constraint specified on the rule, create corresponding clock constraints
     *
     * @param q     The original final state of the left automaton
     * @param right The automaton of the right rule
     */
    private void createClockConstraints(IState q, IRuleAutomaton right) {
        if (_timeConstraint != null) {
            int value = _timeConstraint.getValue();

            q.getTransitions().forEach(t -> t.setClockConstraint(RuleUtils.getRightmostAtom(_left).getEventType(),
                    value));

            if (_timeConstraint.isTransitive()) {
                right.getTransitions().forEach(t -> t.setClockConstraint(RuleUtils.getRightmostAtom(_left).getEventType(), value));
            }
        }
    }
}
