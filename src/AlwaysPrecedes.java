/**
 * Created by William Braik on 6/25/2015.
 */
public class AlwaysPrecedes extends AbstractBinaryRule {

    public AlwaysPrecedes(EventType left, EventType right) {
        super(RuleType.RULE_ALWAYS_PRECEDES, left, right);
    }

    @Override
    public String toString() {
        return _left.toString() + " <-- " + _right.toString();
    }
}
