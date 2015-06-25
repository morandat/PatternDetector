/**
 * Created by William Braik on 6/25/2015.
 */
public abstract class AbstractBinaryRule extends AbstractRule implements BinaryRule {

    EventType _left;
    EventType _right;

    public AbstractBinaryRule(RuleType type, EventType left, EventType right) {
        _type = type;
        _left = left;
        _right = right;
    }

    @Override
    public EventType getLeft() {
        return _left;
    }

    @Override
    public EventType getRight() {
        return _right;
    }
}
