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

    public KleeneNotContiguous(IRule r) {
        super(RuleType.RULE_KLEENE, "+", r);

        try {
            buildAutomaton();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void buildAutomaton() throws Exception {

    }
}
