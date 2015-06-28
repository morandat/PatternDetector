package fr.labri.streamchecking.rules;

import fr.labri.streamchecking.automaton.IAutomaton;

/**
 * Created by William Braik on 6/25/2015.
 */
public class And extends AbstractBinaryRule {

    public And(IRule left, IRule right) {
        super(RuleType.RULE_AND, "&&", left, right);
    }

    @Override
    public IAutomaton buildAutomaton() {
        return null; //TODO
    }
}
