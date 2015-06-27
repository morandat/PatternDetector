/**
 * Created by William Braik on 6/28/2015.
 */
public class Not extends AbstractUnaryRule {

    public Not(IRule r) {
        super(RuleType.RULE_NOT, "!", r);
    }
}
