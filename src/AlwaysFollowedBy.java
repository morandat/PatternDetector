/**
 * Created by William Braik on 6/25/2015.
 */
public class AlwaysFollowedBy extends AbstractBinaryRule {

    public AlwaysFollowedBy(IRule left, IRule right) {
        super(RuleType.RULE_ALWAYS_FOLLOWED_BY, "-->", left, right);
    }
}
