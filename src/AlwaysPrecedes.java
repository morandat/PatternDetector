/**
 * Created by William Braik on 6/25/2015.
 */
public class AlwaysPrecedes extends AbstractBinaryRule {

    public AlwaysPrecedes(IRule left, IRule right) {
        super(RuleType.RULE_ALWAYS_PRECEDES, "<--", left, right);
    }
}
