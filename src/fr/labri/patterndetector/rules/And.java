package fr.labri.patterndetector.rules;

/**
 * Created by William Braik on 6/25/2015.
 */
public class And extends AbstractBinaryRule {

    public And(IRule left, IRule right) {
        super(RuleType.RULE_AND, "&&", left, right);
    }
}
