package fr.labri.streamchecking.rules;

import fr.labri.streamchecking.automaton.IAutomaton;

/**
 * Created by William Braik on 6/25/2015.
 */
public class AlwaysFollowedBy extends AbstractBinaryRule {

    public AlwaysFollowedBy(IRule left, IRule right) {
        super(RuleType.RULE_ALWAYS_FOLLOWED_BY, "-->", left, right);
    }

    @Override
    public IAutomaton buildAutomaton() {
        return null; //TODO
    }
}
