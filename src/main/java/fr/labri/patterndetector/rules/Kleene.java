package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

/**
 * Created by william.braik on 10/07/2015.
 */

/**
 * This rule does not have any termination criterion. The automaton therefore does not have any final state.
 * A way to make this operator terminate is to append an atom (or a rule that terminates) at the end of this rule ;
 * Or to specify a time constraint on the atom.
 * Ex : a+ doesn't terminate but a+ -> b, or a+|10| terminates.
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

    public void buildAutomaton() throws Exception {
        IRuleAutomaton base = AutomatonUtils.copy(_rule.getAutomaton());

        IRuleAutomaton automaton = new RuleAutomaton(this);

        // The initial state of the base component becomes the initial state of the Kleene automaton.
        IState baseInitialState = base.getInitialState();
        automaton.registerInitialState(baseInitialState);

        // The rest of the base component's states become states of the Kleene automaton.
        base.getStates().values().forEach(automaton::registerState);

        // The final state of the base component becomes a state of the Kleene automaton.
        IState baseFinalState = base.getFinalState();
        automaton.registerState(baseFinalState);

        /* --- Add extra stuff to obtain the final Kleene automaton --- */

        // State for ignoring irrelevant events occuring between left and right.
        IState s = new State();
        baseFinalState.registerEpsilonTransition(s);
        s.registerStarTransition(s, TransitionType.TRANSITION_DROP);
        s.registerEpsilonTransition(baseInitialState);
        automaton.registerState(s);
        _connectionStateLabel = s.getLabel();

        //System.err.println(automaton); // TODO for debug

        _automaton = automaton;
    }
}
