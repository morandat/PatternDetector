/**
 * Created by William Braik on 6/25/2015.
 */
public class And extends AbstractRuleComposite {

    public And(Rule left, Rule right) {
        super(RuleType.RULE_AND, left, right);
    }

    @Override
    public String toString() {
        return "( " + _left.toString() + " ) && ( " + _right.toString() + " )";
    }
}
