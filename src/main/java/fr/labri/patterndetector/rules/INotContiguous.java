package fr.labri.patterndetector.rules;

/**
 * Created by William Braik on 8/24/2015.
 */
public interface INotContiguous {

    /**
     * Non contiguous rules can define negation rules. Negation rules are anti-rules that are applied between
     * left and right components for the FollowedBy operator, and between each component of a Kleene sequence.
     * This method adds a new anti-rule to a non-contiguous rule.
     *
     * @param rule The anti-rule to add to the rule.
     */
    void addRuleNegation(IRule rule);
}
