package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

/**
 * Created by William Braik on 6/25/2015.
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
        IRuleAutomaton left = AutomatonUtils.copy(_left.getAutomaton());
        IRuleAutomaton right = AutomatonUtils.copy(_right.getAutomaton());

        //System.err.println(left); // TODO for debug
        //System.err.println(right); // TODO for debug

        IRuleAutomaton automaton = new RuleAutomaton(this);

        /* --- Left component --- */

        // The initial state of the left component becomes the initial state of the FollowedBy automaton.
        automaton.registerInitialState(left.getInitialState());

        // The rest of the left component's states become states of the FollowedBy automaton.
        left.getStates().values().forEach(automaton::registerState);

        // The connection state of the left component becomes a state of the FollowedBy automaton.
        IState leftConnectionState = left.getStateByLabel(_left.getConnectionStateLabel());
        if (State.LABEL_FINAL.equals(leftConnectionState.getLabel())) {
            automaton.registerState(leftConnectionState);
        }

        /* --- Right component --- */

        // The initial state of the right component becomes a state of the FollowedBy automaton.
        IState rightInitialState = right.getInitialState();
        automaton.registerState(rightInitialState);

        // The rest of the right component's states become states of the FollowedBy automaton.
        right.getStates().values().forEach(automaton::registerState);

        // The final state of the FollowedBy automaton is the connection state of the right component's automaton.
        IState rightConnectionState = right.getStateByLabel(_right.getConnectionStateLabel());
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

        // Connect the left component's connection state to s, and s to the right component's initial state, with epsilon transitions.
        leftConnectionState.registerEpsilonTransition(s);
        s.registerEpsilonTransition(rightInitialState);

        _automaton = automaton;

        System.err.println(automaton); // TODO for debug

        // Create clock constraints.
        createClockConstraints(leftConnectionState);
    }

    /**
     * Create clock constraints according to the time constraint specified for the rule.
     *
     * @param leftConnectionState The connection state of the left automaton.
     */
    private void createClockConstraints(IState leftConnectionState) {
        if (_timeConstraint != null) {
            int value = _timeConstraint.getValue();

            leftConnectionState.getTransitions().forEach(t ->
                    t.setClockConstraint(RuleUtils.getRightmostAtom(_left).getEventType(), value));
        }
    }
}
