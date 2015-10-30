package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;
import fr.labri.patterndetector.automaton.exception.RuleAutomatonException;

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
    public void buildAutomaton() throws RuleAutomatonException {
        IRuleAutomaton leftAutomaton = _leftChild.getAutomaton().copy();
        IRuleAutomaton rightAutomaton = _rightChild.getAutomaton().copy();

        IRuleAutomaton automaton = new RuleAutomaton(this);

        /* --- Left component --- */

        // The initial state of the left component becomes the initial state of the FollowedBy automaton.
        automaton.setInitialState(leftAutomaton.getInitialState());

        // The connection state of the left component becomes a state of the FollowedBy automaton.
        IState leftConnectionState = leftAutomaton.getState(_leftChild.getConnectionStateLabel());
        // If the connection state is a final state, register it as a basic state
        if (State.LABEL_FINAL.equals(leftConnectionState.getLabel())) {
            automaton.addState(leftConnectionState);
        }

        // The rest of the left component's states become states of the FollowedBy automaton.
        leftAutomaton.getStates().forEach(automaton::addState);

        /* --- Right component --- */

        // The initial state of the right component becomes a state of the FollowedBy automaton.
        IState rightInitialState = rightAutomaton.getInitialState();
        automaton.addState(rightInitialState);

        // The final state of the FollowedBy automaton is the connection state of the right component's automaton.
        IState rightConnectionState = rightAutomaton.getState(_rightChild.getConnectionStateLabel());
        automaton.setFinalState(rightConnectionState);
        _connectionStateLabel = rightConnectionState.getLabel();

        // The rest of the right component's states become states of the FollowedBy automaton.
        rightAutomaton.getStates().forEach(automaton::addState);

        /* --- Add extra stuff to obtain the final FollowedBy automaton. --- */

        // State for ignoring irrelevant events occuring between left and right.
        State s = new State();

        // If the left automaton is Kleene, then the negation transition is already on the connection state.
        if (!(_leftChild instanceof Kleene)) { // TODO this code is ugly
            s.registerStarTransition(s, TransitionType.TRANSITION_DROP);
        }

        automaton.addState(s);

        // Connect the left component's connection state to s, and s to the right component's initial state,
        // with epsilon transitions.
        leftConnectionState.registerEpsilonTransition(s);
        s.registerEpsilonTransition(rightInitialState);

        _automaton = automaton;

        // If a time constraint is specified, create clock constraints now.
        createClockGuards();
    }

    /**
     * Create clock guards corresponding to the time constraint specified for the rule.
     */
    //@Override
    private void createClockGuards() {
        if (_timeConstraint != null) {
            int value = _timeConstraint.getValue();

            /*leftConnectionState.getTransitions().forEach(t ->
                    t.setClockGuard(_leftChild.getRightmostAtom().getEventType(), value));
            rightAutomaton.getTransitions().forEach(t ->
                    t.setClockGuard(_leftChild.getRightmostAtom().getEventType(), value));*/
        }
    }

    @Override
    public void accept(RuleVisitor visitor) {
        //_leftChild.accept(visitor); // TODO do we need this?
        //_rightChild.accept(visitor);

        visitor.visit(this);
    }
}
