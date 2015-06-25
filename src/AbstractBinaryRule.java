/**
 * Created by William Braik on 6/25/2015.
 */
public abstract class AbstractBinaryRule implements BinaryRule {

    EventType _left;
    EventType _right;

    public AbstractBinaryRule() {

    }

    public AbstractBinaryRule(EventType left, EventType right) {
        _left = left;
        _right = right;
    }

    public void print() {

    }
}
