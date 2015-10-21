package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

/**
 * Created by William Braik on 6/25/2015.
 * <p>
 * The FollowedBy rule captures the occurrence of a rule (left operand), followed by another rule (right operand).
 * TODO In the case where the left side is matched The selection policy (first, each, or last) determines which of the captured events on the left side are selected.
 */
public class FollowedBy extends AbstractBinaryRule {

    public static final String Symbol = "-->";

    public FollowedBy(IRule left, IRule right) {
        super(FollowedBy.Symbol, left, right);
    }

    public FollowedBy(String e, IRule right) {
        super(FollowedBy.Symbol,
                (e.startsWith("!") ? new AtomNot(e.substring(1)) : new Atom(e)),
                right);
    }

    public FollowedBy(IRule left, String e) {
        super(FollowedBy.Symbol,
                left,
                (e.startsWith("!") ? new AtomNot(e.substring(1)) : new Atom(e)));
    }

    public FollowedBy(String e1, String e2) {
        super(FollowedBy.Symbol,
                (e1.startsWith("!") ? new AtomNot(e1.substring(1)) : new Atom(e1)),
                (e2.startsWith("!") ? new AtomNot(e2.substring(1)) : new Atom(e2)));
    }

    @Override
    public void buildAutomaton() throws Exception {
        IRuleAutomaton leftAutomaton = AutomatonUtils.copy(_left.getAutomaton());
        IRuleAutomaton rightAutomaton = AutomatonUtils.copy(_right.getAutomaton());

        //System.err.println(left); // TODO for debug
        //System.err.println(right); // TODO for debug

        IRuleAutomaton automaton = new RuleAutomaton(this);

        /* --- Left component --- */

        // The initial state of the left component becomes the initial state of the FollowedBy automaton.
        automaton.registerInitialState(leftAutomaton.getInitialState());

        // The rest of the left component's states become states of the FollowedBy automaton.
        leftAutomaton.getStates().values().forEach(automaton::registerState);

        // The connection state of the left component becomes a state of the FollowedBy automaton.
        IState leftConnectionState = leftAutomaton.getStateByLabel(_left.getConnectionStateLabel());
        if (State.LABEL_FINAL.equals(leftConnectionState.getLabel())) {
            automaton.registerState(leftConnectionState);
        }

        /* --- Right component --- */

        // The initial state of the right component becomes a state of the FollowedBy automaton.
        IState rightInitialState = rightAutomaton.getInitialState();
        automaton.registerState(rightInitialState);

        // The rest of the right component's states become states of the FollowedBy automaton.
        rightAutomaton.getStates().values().forEach(automaton::registerState);

        // The final state of the FollowedBy automaton is the connection state of the right component's automaton.
        IState rightConnectionState = rightAutomaton.getStateByLabel(_right.getConnectionStateLabel());
        automaton.registerFinalState(rightConnectionState);
        _connectionStateLabel = rightConnectionState.getLabel();

        /* --- Add extra stuff to obtain the final FollowedBy automaton. --- */

        // State for ignoring irrelevant events occuring between left and right.
        State s = new State();

        // If the left automaton is Kleene, then the negation transition is already on the connection state.
        if (_left instanceof Kleene) {
            s.registerStarTransition(s, TransitionType.TRANSITION_DROP);
        }

        automaton.registerState(s);

        // Connect the left component's connection state to s, and s to the right component's initial state,
        // with epsilon transitions.
        leftConnectionState.registerEpsilonTransition(s);
        s.registerEpsilonTransition(rightInitialState);

        _automaton = automaton;

        System.err.println(automaton); // TODO for debug

        // Create clock constraints.
        createClockConstraints(leftConnectionState, rightAutomaton);
    }

    /**
     * Create clock constraints according to the time constraint specified for the rule.
     *
     * @param leftConnectionState The connection state of the left automaton.
     * @param rightAutomaton      The right automaton.
     */
    private void createClockConstraints(IState leftConnectionState, IRuleAutomaton rightAutomaton) {
        if (_timeConstraint != null) {
            int value = _timeConstraint.getValue();

            leftConnectionState.getTransitions().forEach(t ->
                    t.setClockConstraint(RuleUtils.getRightmostAtom(_left).getEventType(), value));

            rightAutomaton.getTransitions().forEach(t ->
                    t.setClockConstraint(RuleUtils.getRightmostAtom(_left).getEventType(), value));
        }
    }
}
