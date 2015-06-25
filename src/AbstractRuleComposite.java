/**
 * Created by William Braik on 6/25/2015.
 */
public abstract class AbstractRuleComposite implements RuleComposite {

    RuleType _type;
    Rule _left;
    Rule _right;

    public AbstractRuleComposite(RuleType type, Rule left, Rule right) {
        _type = type;
        _left = left;
        _right = right;
    }

    @Override
    public RuleType getType() {
        return _type;
    }

    @Override
    public Rule getLeft() {
        return _left;
    }

    @Override
    public Rule getRight() {
        return _right;
    }
}
