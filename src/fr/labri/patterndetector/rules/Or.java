package fr.labri.patterndetector.rules;

/**
 * Created by William Braik on 6/25/2015.
 */
public class Or extends AbstractBinaryRule {

    public static final String Symbol = "||";

    public Or(IRule left, IRule right) {
        super(RuleType.RULE_OR, Or.Symbol, left, right);
    }
}
