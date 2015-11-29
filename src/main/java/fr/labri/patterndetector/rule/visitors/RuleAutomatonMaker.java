package fr.labri.patterndetector.rule.visitors;

import fr.labri.patterndetector.automaton.*;
import fr.labri.patterndetector.automaton.exception.RuleAutomatonException;
import fr.labri.patterndetector.rule.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by wbraik on 20/11/15.
 * <p>
 * Compiles a rule into a corresponding automaton by traversing the rule tree.
 */
public final class RuleAutomatonMaker {

    private RuleAutomatonMaker() {
    }

    public static IRuleAutomaton makeAutomaton(IRule rule) {
        RuleAutomatonMakerVisitor visitor = new RuleAutomatonMakerVisitor();
        rule.accept(visitor);

        return visitor.getAutomaton();
    }

    static class RuleAutomatonMakerVisitor extends AbstractRuleVisitor {

        private final Logger logger = LoggerFactory.getLogger(RuleAutomatonMakerVisitor.class);

        private IRuleAutomaton _automaton;

        @Override
        public void visit(Atom atom) {
            try {
                IState i = new State(); // Initial state
                IState f = new State(); // Final state

                i.registerTransition(f, atom.getEventType(), TransitionType.TRANSITION_APPEND, atom.getPredicates());

                IRuleAutomaton automaton = new RuleAutomaton();
                automaton.setInitialState(i);
                automaton.addFinalState(f);
                automaton.addConnectionState(f);

                _automaton = automaton;
            } catch (RuleAutomatonException e) {
                logger.error("Compilation failed : " + e.getMessage() + "(" + atom + ")\n"
                        + e.getRuleAutomaton());

                throw new RuntimeException("Compilation failed : " + e.getMessage() + "(" + atom + ")");
            }
        }

        // TODO visit(AtomNot atomNot)

        @Override
        public void visit(FollowedBy followedBy) {
            try {
                IRuleAutomaton leftAutomaton = RuleAutomatonMaker.makeAutomaton(followedBy.getLeftChildRule());
                IRuleAutomaton rightAutomaton = RuleAutomatonMaker.makeAutomaton(followedBy.getRightChildRule());
                IRuleAutomaton automaton = new RuleAutomaton();

                // ### Left component

                // The initial state of the left component becomes the initial state of the FollowedBy automaton.
                automaton.setInitialState(leftAutomaton.getInitialState());

                // The connection states of the left component become states of the FollowedBy automaton.
                leftAutomaton.getConnectionStates().forEach(connectionState -> {
                    // If the connection state is a final state, register it as a basic state
                    if (connectionState.isFinal()) {
                        automaton.addState(connectionState);
                    }
                });

                // The rest of the left component's states become states of the FollowedBy automaton.
                leftAutomaton.getStates().forEach(automaton::addState);

                // ### Right component

                // The initial state of the right component becomes a state of the FollowedBy automaton.
                IState rightInitialState = rightAutomaton.getInitialState();
                automaton.addState(rightInitialState);

                // The final states of the FollowedBy automaton are the connection states of the right component's automaton.
                rightAutomaton.getConnectionStates().forEach(connectionState -> {
                    automaton.addFinalState(connectionState);
                    automaton.addConnectionState(connectionState);
                });

                // The rest of the right component's states become states of the FollowedBy automaton.
                rightAutomaton.getStates().forEach(automaton::addState);

                // ### Add extra stuff to obtain the final FollowedBy automaton.

                // State for ignoring irrelevant events occuring between left and right.
                State s = new State();

                // If the left automaton is Kleene, then the negation transition is already on the connection state.
                s.registerStarTransition(s, TransitionType.TRANSITION_DROP);

                automaton.addState(s);

                // Connect the left component's connection states to s, and s to the right component's initial state,
                // with epsilon transitions.

                leftAutomaton.getConnectionStates().forEach(connectionState -> connectionState.registerEpsilonTransition(s));
                s.registerEpsilonTransition(rightInitialState);

                // If a time constraint is specified, create clock constraints.
                if (followedBy.getTimeConstraint() != null) {
                    int value = followedBy.getTimeConstraint().getValue();

                    rightAutomaton.getTransitions().forEach(t ->
                            t.setClockGuard(followedBy.getLeftChildRule().getRightmostAtom().getEventType(), value));
                    s.getTransitionsByLabel(Transition.LABEL_STAR).get(0)
                            .setClockGuard(followedBy.getLeftChildRule().getRightmostAtom().getEventType(), value);
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
                IRuleAutomaton baseAutomaton = RuleAutomatonMaker.makeAutomaton(kleene.getChildRule());
                IRuleAutomaton automaton = new RuleAutomaton();

                // The initial state of the base component becomes the initial state of the Kleene automaton.
                IState baseInitialState = baseAutomaton.getInitialState();
                automaton.setInitialState(baseInitialState);

                // The final states of the base component become states of the Kleene automaton.
                baseAutomaton.getFinalStates().forEach(finalState -> {
                    automaton.addState(finalState);
                    automaton.addConnectionState(finalState);
                });

                // The rest of the base component's states become states of the Kleene automaton.
                baseAutomaton.getStates().forEach(automaton::addState);

                // ### Add extra stuff to obtain the final Kleene automaton.

                // State for ignoring irrelevant events occuring between each event captured by the Kleene sequence.
                IState s = new State();
                baseAutomaton.getFinalStates().forEach(finalState -> finalState.registerEpsilonTransition(s));
                s.registerStarTransition(s, TransitionType.TRANSITION_DROP);
                s.registerEpsilonTransition(baseInitialState);
                automaton.addState(s);

                // If a time constraint is specified, create clock constraints.
                if (kleene.getTimeConstraint() != null) {
                    int value = kleene.getTimeConstraint().getValue();

                    baseAutomaton.getInitialState().getTransitions().forEach(t ->
                            t.setClockGuard(kleene.getChildRule().getRightmostAtom().getEventType(), value));
                    s.getTransitionsByLabel(Transition.LABEL_STAR).get(0)
                            .setClockGuard(kleene.getChildRule().getRightmostAtom().getEventType(), value);
                }

                _automaton = automaton;
            } catch (RuleAutomatonException e) {
                logger.error("Compilation failed : " + e.getMessage() + "(" + kleene + ")\n"
                        + e.getRuleAutomaton());

                throw new RuntimeException("Compilation failed : " + e.getMessage() + "(" + kleene + ")");
            }
        }

        @Override
        public void visit(Or or) {
            try {
                IRuleAutomaton leftAutomaton = RuleAutomatonMaker.makeAutomaton(or.getLeftChildRule());
                IRuleAutomaton rightAutomaton = RuleAutomatonMaker.makeAutomaton(or.getRightChildRule());
                IRuleAutomaton automaton = new RuleAutomaton();

                // ### Left component

                // The initial state of the left component becomes a state of the Or automaton.
                IState leftInitialState = leftAutomaton.getInitialState();
                automaton.addState(leftInitialState);

                // The left component's states become states of the Or automaton.
                leftAutomaton.getStates().forEach(automaton::addState);

                // The left component's final states become final states of the Or automaton.
                leftAutomaton.getFinalStates().forEach(finalState -> {
                    automaton.addFinalState(finalState);
                    automaton.addConnectionState(finalState);
                });

                // ### Right component

                // The initial state of the right component becomes a state of the Or automaton.
                IState rightInitialState = rightAutomaton.getInitialState();
                automaton.addState(rightInitialState);

                // The right component's states become states of the FollowedBy automaton.
                rightAutomaton.getStates().forEach(automaton::addState);

                // The right component's final states become final states of the Or automaton.
                rightAutomaton.getFinalStates().forEach(finalState -> {
                    automaton.addFinalState(finalState);
                    automaton.addConnectionState(finalState);
                });
                // ### Add extra stuff to obtain the final FollowedBy automaton.

                // Initial state
                State i = new State();

                // If the left automaton is Kleene, then the negation transition is already on the connection state.
                i.registerEpsilonTransition(leftInitialState);
                i.registerEpsilonTransition(rightInitialState);

                automaton.setInitialState(i);

                _automaton = automaton;
            } catch (RuleAutomatonException e) {
                logger.error("Compilation failed : " + e.getMessage() + "(" + or + ")\n"
                        + e.getRuleAutomaton());

                throw new RuntimeException("Compilation failed : " + e.getMessage() + "(" + or + ")");
            }
        }

        public IRuleAutomaton getAutomaton() {
            return _automaton;
        }
    }
}
