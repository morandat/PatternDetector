package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.IAutomaton;

/**
 * Created by William Braik on 6/25/2015.
 */
public class Or extends AbstractBinaryRule {

    public Or(IRule left, IRule right) {
        super(RuleType.RULE_OR, "||", left, right);
    }

    public Or(IRule left, IRule right, TimeConstraint tc) {
        super(RuleType.RULE_OR, "||", left, right, tc);
    }

    @Override
    public IAutomaton buildAutomaton() {
        return null; //TODO
    }
}
