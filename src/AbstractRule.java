/**
 * Created by William Braik on 6/25/2015.
 */
public abstract class AbstractRule implements Rule {

    RuleType _type;

    @Override
    public RuleType getType() {
        return _type;
    }
}
