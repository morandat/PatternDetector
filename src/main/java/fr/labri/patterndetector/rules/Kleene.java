package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;
import fr.labri.patterndetector.automaton.exception.RuleAutomatonException;

/**
 * Created by william.braik on 10/07/2015.
 * <p>
 * The Kleene rule captures at least one occurrence of an event type.
 * Because the stream of events is infinite, it does not have any termination criterion by default.
 * Therefore, the corresponding Kleene automaton does not have any final state.
 * However, Kleene can be connected to other rules using the FollowedBy operator.
 * In that case, Kleene terminates as soon as the following rule begins.
 * TODO It can also be used on its own, if a window is specified for the pattern.
 */
public class Kleene extends AbstractUnaryRule {

    public static final String Symbol = "+";

    public Kleene(IRule r) {
        super(Kleene.Symbol, r);
    }

    public Kleene(String e) {
        super(Kleene.Symbol,
                (e.startsWith("!") ? new AtomNot(e.substring(1)) : new Atom(e)));
    }

    public void buildAutomaton() throws RuleAutomatonException {
        IRuleAutomaton baseAutomaton = _childRule.getAutomaton().copy();

        IRuleAutomaton automaton = new RuleAutomaton(this);

        // The initial state of the base component becomes the initial state of the Kleene automaton.
        IState baseInitialState = baseAutomaton.getInitialState();
        automaton.setInitialState(baseInitialState);

        // The final state of the base component becomes a state of the Kleene automaton.
        IState baseFinalState = baseAutomaton.getFinalState();
        automaton.addState(baseFinalState);
        _connectionStateLabel = baseFinalState.getLabel();

        // The rest of the base component's states become states of the Kleene automaton.
        baseAutomaton.getStates().forEach(automaton::addState);

        /* --- Add extra stuff to obtain the final Kleene automaton --- */

        // State for ignoring irrelevant events occuring between left and right.
        IState s = new State();
        baseFinalState.registerEpsilonTransition(s);
        s.registerStarTransition(s, TransitionType.TRANSITION_DROP);
        s.registerEpsilonTransition(baseInitialState);
        automaton.addState(s);

        _automaton = automaton;
    }

    @Override
    public void accept(RuleVisitor visitor) {
        //_childRule.accept(visitor); // TODO do we need this?

        visitor.visit(this);
    }
}
