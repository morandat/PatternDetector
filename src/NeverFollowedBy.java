/**
 * Created by William Braik on 6/25/2015.
 */
public class NeverFollowedBy extends AbstractBinaryRule {

    public NeverFollowedBy(EventType left, EventType right) {
        super(RuleType.RULE_NEVER_FOLLOWED_BY, left, right);
    }

    @Override
    public String toString() {
        return _left.toString() + " -/-> " + _right.toString();
    }
}
