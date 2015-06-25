/**
 * Created by William Braik on 6/25/2015.
 */
public class Or extends AbstractRuleComposite {

    public Or(Rule left, Rule right) {
        super(RuleType.RULE_OR, left, right);
    }

    @Override
    public String toString() {
        return "( " + _left.toString() + " ) || ( " + _right.toString() + " )";
    }
}
