/**
 * Created by William Braik on 6/25/2015.
 */
public class AlwaysFollowedBy extends AbstractBinaryRule {

    public AlwaysFollowedBy(EventType left, EventType right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return _left.toString() + " -> " + _right.toString();
    }
}
