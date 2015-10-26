import fr.labri.patterndetector.executor.*;
import fr.labri.patterndetector.rules.Atom;
import fr.labri.patterndetector.rules.FollowedBy;
import fr.labri.patterndetector.rules.IRule;
import fr.labri.patterndetector.rules.Kleene;
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
    private RuleManager _ruleManager;
    private Detector _detector;

    @Rule
    public TestName _name = new TestName();

    @Before
    public void initializeContext() {
        logger.info("Executing test : " + _name.getMethodName());
        _ruleManager = new RuleManager();
        _detector = new Detector(_ruleManager);

    }

    @Test
    public void testAtom() {
        IRule r = new Atom("a");

        _ruleManager.addRule(r);
        _detector.detect(Generator.generateStuff());

        Collection<IEvent> expected = new ArrayList<>();
        expected.add(new Event("a", 1));
        expected.add(new Event("a", 2));
        expected.add(new Event("a", 3));
        expected.add(new Event("a", 4));
        expected.add(new Event("a", 5));

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFollowedBy() {
        IRule r = new FollowedBy("a", "b");

        _ruleManager.addRule(r);
        _detector.detect(Generator.generateStuff());

        Collection<IEvent> expected = new ArrayList<>();
        expected.add(new Event("a", 1));
        expected.add(new Event("a", 2));
        expected.add(new Event("a", 3));
        expected.add(new Event("a", 4));
        expected.add(new Event("a", 5));
        expected.add(new Event("b", 9));
        expected.add(new Event("c", 11));

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testKleene() {

    }
}
