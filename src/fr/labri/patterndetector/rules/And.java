package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.IAutomaton;

/**
 * Created by William Braik on 6/25/2015.
 */
public class And extends AbstractBinaryRule {

    public And(IRule left, IRule right) {
        super(RuleType.RULE_AND, "&&", left, right);
    }

    @Override
    public IAutomaton buildAutomaton() throws Exception {
        return null; //TODO
    }
}
