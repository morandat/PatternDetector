import fr.labri.patterndetector.executor.*;
import fr.labri.patterndetector.rules.Atom;
import fr.labri.patterndetector.rules.FollowedBy;
import fr.labri.patterndetector.rules.IRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by william.braik on 14/08/2015.
 * <p>
 * Test suite for pattern detection scenarios.
 */
public class PatternDetectionTest {

    private final Logger logger = LoggerFactory.getLogger(PatternDetectionTest.class);

    private RuleManager _ruleManager = new RuleManager();
    private Detector _detector = new Detector(_ruleManager);
    private Generator _generator = new Generator();

    @Rule
    public TestName _name = new TestName();

    @Before
    public void initializeContext() {
        logger.info("Executing test : " + _name.getMethodName());

        _ruleManager.removeAllRules();
        _generator.resetTime();
    }

    @Test
    public void testAtom() {
        IRule r = new Atom("a");

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateAtom());

        Collection<IEvent> expected = new ArrayList<>();
        expected.add(new Event("a", 1));

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFollowedBy() {
        IRule r = new FollowedBy("a", "b");

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateFollowedBy());

        Collection<IEvent> expected = new ArrayList<>();
        expected.add(new Event("a", 1));
        expected.add(new Event("b", 4));

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testKleene() {

    }

    // TODO write those tests
    /*IRule rule = new FollowedBy("a", "a");

    Kleene rule = new Kleene("View");

    IRule rule = new FollowedBy(new Kleene("View"), "Exit");

    IRule rule = new FollowedBy("Enter", new Kleene("View"));

    IRule rule = new FollowedBy(new Kleene("View"), new FollowedBy("Add", "Exit"));*/
}
