package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.IAutomaton;

/**
 * Created by William Braik on 6/25/2015.
 */
public class NotFollowedBy extends AbstractBinaryRule {

    public NotFollowedBy(IRule left, IRule right) {
        super(RuleType.RULE_NOT_FOLLOWED_BY, "-/->", left, right);
    }
}
