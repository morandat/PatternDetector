package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.EventType;
import fr.labri.patterndetector.automaton.*;

/**
 * Created by William Braik on 6/25/2015.
 */
public class ImmediatelyFollowedBy extends AbstractBinaryRule {

    public ImmediatelyFollowedBy(IRule left, IRule right) {
        super(RuleType.RULE_FOLLOWED_BY, ".", left, right);

        try {
            buildAutomaton();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void buildAutomaton() throws Exception {

    }
}
