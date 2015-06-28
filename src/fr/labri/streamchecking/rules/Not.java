package fr.labri.streamchecking.rules;

import fr.labri.streamchecking.automaton.IAutomaton;

/**
 * Created by William Braik on 6/28/2015.
 */
public class Not extends AbstractUnaryRule {

    public Not(IRule r) {
        super(RuleType.RULE_NOT, "!", r);
    }

    @Override
    public IAutomaton buildAutomaton() {
        return null; //TODO
    }
}
