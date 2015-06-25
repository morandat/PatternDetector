/**
 * Created by William Braik on 6/25/2015.
 */
public abstract class AbstractBinaryRuleComposite implements BinaryRule {

    BinaryRule _left;
    BinaryRule _right;

    public AbstractBinaryRuleComposite(BinaryRule left, BinaryRule right) {
        _left = left;
        _right = right;
    }

    public void print() {

    }
}
