/**
 * Created by William Braik on 6/25/2015.
 */
public class NeverFollowedBy extends AbstractBinaryRule {

    public NeverFollowedBy(IRule left, IRule right) {
        super(RuleType.RULE_NEVER_FOLLOWED_BY, "-/->", left, right);
    }
}
