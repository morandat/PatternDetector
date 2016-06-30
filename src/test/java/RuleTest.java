import fr.labri.patterndetector.rule.FollowedBy;
import fr.labri.patterndetector.rule.IRule;
import fr.labri.patterndetector.rule.Kleene;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wbraik on 23/11/15.
 */
public class RuleTest {

    private final Logger Logger = LoggerFactory.getLogger(RuleTest.class);

    @Rule
    public TestName _name = new TestName();

    @Before
    public void initializeTest() {
        Logger.info("## Executing test : " + _name.getMethodName());
    }

    @Test
    public void shouldFindRightmostAtom() {
        IRule r = new FollowedBy(new Kleene("x"), new FollowedBy("b", new Kleene("c")));

        String expected = "c";

        String actual = r.getRightmostAtom().getEventType();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldFindLeftmostAtom() {
        IRule r = new FollowedBy(new Kleene("x"), new FollowedBy("b", new Kleene("c")));

        String expected = "x";

        String actual = r.getLeftmostAtom().getEventType();

        Assert.assertEquals(expected, actual);
    }
}
