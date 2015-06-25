/**
 * Created by William Braik on 6/25/2015.
 */
public class And extends AbstractBinaryRuleComposite {

    public And(BinaryRule left, BinaryRule right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return "( " + _left.toString() + " ) && ( " + _right.toString() + " )";
    }
}
