/**
 * Created by William Braik on 6/25/2015.
 */
public interface RuleComposite extends Rule {

    public Rule getLeft();

    public Rule getRight();
}
