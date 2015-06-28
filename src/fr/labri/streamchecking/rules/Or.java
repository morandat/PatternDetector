package fr.labri.streamchecking.rules;

import fr.labri.streamchecking.automaton.IAutomaton;

/**
 * Created by William Braik on 6/25/2015.
 */
public class Or extends AbstractBinaryRule {

    public Or(IRule left, IRule right) {
        super(RuleType.RULE_OR, "||", left, right);
    }

    @Override
    public IAutomaton buildAutomaton() {
        return null; //TODO
    }
}
