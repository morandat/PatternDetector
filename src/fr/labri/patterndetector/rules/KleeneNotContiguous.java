package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

/**
 * Created by william.braik on 10/07/2015.
 */

/**
 * This rule does not have any termination criterion. The automaton therefore does not have any final state.
 * A way to make this operator terminate is to append an atom (or a rule that terminates) at the end of this rule ;
 * Or to specify a time constraint on the atom.
 * Ex : a++ doesn't terminate but a++ -> b terminates.
 */
public class KleeneNotContiguous extends AbstractUnaryRule {

    public KleeneNotContiguous(Atom x) {
        super(RuleType.RULE_KLEENE, "+", x);

        try {
            buildAutomaton();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void buildAutomaton() throws Exception {
        /*Atom x = (Atom) _r;

        IState s0 = new State();
        IState s1 = new State();
        IState s2 = new State();

        s0.registerTransition(x.getEventType(), s1);
        s1.registerTransition(x.getEventType(), s1);
        s1.registerTransition(AutomatonUtils.negationTransitionLabel(), s2);
        s2.registerTransition(x.getEventType(), s1);
        s2.registerTransition(AutomatonUtils.negationTransitionLabel(), s2);

        IAutomaton automaton = new Automaton();
        automaton.registerInitialState(s0);
        automaton.registerState(s1);
        automaton.registerState(s2);

        _automaton = automaton;*/

        //TODO
    }
}