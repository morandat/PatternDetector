package fr.labri.patterndetector.compiler;

import fr.labri.patterndetector.automaton.*;
import fr.labri.patterndetector.automaton.exception.RuleAutomatonException;
import fr.labri.patterndetector.rules.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wbraik on 20/11/15.
 * <p>
 * Compiles a rule into a corresponding automaton by traversing the rule tree.
 */
public class RuleCompiler extends AbstractRuleVisitor {

    private IRuleAutomaton _automaton;

    private final Logger logger = LoggerFactory.getLogger(RuleCompiler.class);

    public IRuleAutomaton compile(IRule rule) {
        rule.accept(this);

        return _automaton;
    }

    @Override
    public void visit(Atom atom) {
        try {
            IState i = new State(); // Initial state
            IState f = new State(); // Final state

            i.registerTransition(f, atom.getEventType(), TransitionType.TRANSITION_APPEND, atom.getPredicates());

            IRuleAutomaton automaton = new RuleAutomaton();
            automaton.setInitialState(i);
            automaton.setFinalState(f);
            automaton.setConnectionState(f);

            _automaton = automaton;
        } catch (RuleAutomatonException e) {
            logger.error("Compilation failed : " + e.getMessage() + "(" + atom + ")\n"
                    + e.getRuleAutomaton());

            throw new RuntimeException("Compilation failed : " + e.getMessage() + "(" + atom + ")");
        }

    }

    @Override
    public void visit(FollowedBy followedBy) {
        try {
            IRuleAutomaton leftAutomaton = new RuleCompiler().compile(followedBy.getLeftChildRule());
            IRuleAutomaton rightAutomaton = new RuleCompiler().compile(followedBy.getRightChildRule());
            IRuleAutomaton automaton = new RuleAutomaton();

            // ### Left component

            // The initial state of the left component becomes the initial state of the FollowedBy automaton.
            automaton.setInitialState(leftAutomaton.getInitialState());

            // The connection state of the left component becomes a state of the FollowedBy automaton.
            IState leftConnectionState = leftAutomaton.getConnectionState();
            // If the connection state is a final state, register it as a basic state
            if (State.LABEL_FINAL.equals(leftConnectionState.getLabel())) {
                automaton.addState(leftConnectionState);
            }

            // The rest of the left component's states become states of the FollowedBy automaton.
            leftAutomaton.getStates().forEach(automaton::addState);

            // ### Right component

            // The initial state of the right component becomes a state of the FollowedBy automaton.
            IState rightInitialState = rightAutomaton.getInitialState();
            automaton.addState(rightInitialState);

            // The final state of the FollowedBy automaton is the connection state of the right component's automaton.
            IState rightConnectionState = rightAutomaton.getConnectionState();
            automaton.setFinalState(rightConnectionState);
            automaton.setConnectionState(rightConnectionState);

            // The rest of the right component's states become states of the FollowedBy automaton.
            rightAutomaton.getStates().forEach(automaton::addState);

            // ### Add extra stuff to obtain the final FollowedBy automaton.

            // State for ignoring irrelevant events occuring between left and right.
            State s = new State();

            // If the left automaton is Kleene, then the negation transition is already on the connection state.
            s.registerStarTransition(s, TransitionType.TRANSITION_DROP);

            automaton.addState(s);

            // Connect the left component's connection state to s, and s to the right component's initial state,
            // with epsilon transitions.
            leftConnectionState.registerEpsilonTransition(s);
            s.registerEpsilonTransition(rightInitialState);

            // TODO If a time constraint is specified, create clock constraints.
            if (followedBy.getTimeConstraint() != null) {
                int value = followedBy.getTimeConstraint().getValue();

                s.getTransitionsByLabel(Transition.LABEL_STAR).get(0)
                        .setClockGuard(followedBy.getLeftChildRule().getRightmostAtom().getEventType(), value);
                rightAutomaton.getTransitions().forEach(t ->
                        t.setClockGuard(followedBy.getLeftChildRule().getRightmostAtom().getEventType(), value));
            }

            _automaton = automaton;
        } catch (RuleAutomatonException e) {
            logger.error("Compilation failed : " + e.getMessage() + "(" + followedBy + ")\n"
                    + e.getRuleAutomaton());

            throw new RuntimeException("Compilation failed : " + e.getMessage() + "(" + followedBy + ")");
        }
    }

    @Override
    public void visit(Kleene kleene) {
        try {
            IRuleAutomaton baseAutomaton = new RuleCompiler().compile(kleene.getChildRule());
            IRuleAutomaton automaton = new RuleAutomaton();

            // The initial state of the base component becomes the initial state of the Kleene automaton.
            IState baseInitialState = baseAutomaton.getInitialState();
            automaton.setInitialState(baseInitialState);

            // The final state of the base component becomes a state of the Kleene automaton.
            IState baseFinalState = baseAutomaton.getFinalState();
            automaton.addState(baseFinalState);
            automaton.setConnectionState(baseFinalState);

            // The rest of the base component's states become states of the Kleene automaton.
            baseAutomaton.getStates().forEach(automaton::addState);

            // ### Add extra stuff to obtain the final Kleene automaton.

            // State for ignoring irrelevant events occuring between left and right.
            IState s = new State();
            baseFinalState.registerEpsilonTransition(s);
            s.registerStarTransition(s, TransitionType.TRANSITION_DROP);
            s.registerEpsilonTransition(baseInitialState);
            automaton.addState(s);

            // TODO If a time constraint is specified, create clock constraints.
            /*if (_timeConstraint != null) {
                int value = _timeConstraint.getValue();

                leftConnectionState.getTransitions().forEach(t ->
                        t.setClockGuard(_leftChild.getRightmostAtom().getEventType(), value));
                rightAutomaton.getTransitions().forEach(t ->
                        t.setClockGuard(_leftChild.getRightmostAtom().getEventType(), value));
            }*/

            _automaton = automaton;
        } catch (RuleAutomatonException e) {
            logger.error("Compilation failed : " + e.getMessage() + "(" + kleene + ")\n"
                    + e.getRuleAutomaton());

            throw new RuntimeException("Compilation failed : " + e.getMessage() + "(" + kleene + ")");
        }
    }
}
