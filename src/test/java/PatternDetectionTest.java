import fr.labri.patterndetector.executor.*;
import fr.labri.patterndetector.rules.Atom;
import fr.labri.patterndetector.rules.FollowedBy;
import fr.labri.patterndetector.rules.IRule;
import fr.labri.patterndetector.rules.Kleene;
import org.hamcrest.core.StringContains;
import org.junit.*;
import org.junit.rules.ExpectedException;
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

    @Rule
    public ExpectedException _thrown = ExpectedException.none();

    @Before
    public void initializeTest() {
        logger.info("## Executing test : " + _name.getMethodName());

        _ruleManager.removeAllRules();
        _generator.resetTime();
    }

    @Test
    public void shouldDetectAtom() {
        IRule r = new Atom("a");

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateAtom());

        Collection<IEvent> expected = new ArrayList<>();
        expected.add(new Event("a", 1));

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldDetectAtomWithPredicate() {
        IRule r = new Atom("a").addPredicate("x", x -> x > 5);

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateAtom());

        Collection<IEvent> expected = new ArrayList<>();
        expected.add(new Event("a", 1));

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldNotDetectAtomWithPredicate() {
        IRule r = new Atom("a").addPredicate("x", x -> x > 20);

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateAtom());

        Collection<IEvent> expected = new ArrayList<>();

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldDetectAFollowedByB() {
        IRule r = new FollowedBy("a", "b");

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateFollowedBy());

        Collection<IEvent> expected = new ArrayList<>();
        expected.add(new Event("a", 1));
        expected.add(new Event("b", 5));

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldDetectAFollowedByA() {
        IRule r = new FollowedBy("a", "a");

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateFollowedBy());

        Collection<IEvent> expected = new ArrayList<>();
        expected.add(new Event("a", 1));
        expected.add(new Event("a", 3));

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldDetectASeqFollowedByB() {
        IRule r = new FollowedBy(new Kleene("a"), "b");

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateKleene());

        Collection<IEvent> expected = new ArrayList<>();
        expected.add(new Event("a", 1));
        expected.add(new Event("a", 3));
        expected.add(new Event("a", 4));
        expected.add(new Event("a", 6));
        expected.add(new Event("b", 7));

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldDetectASeqFollowedByBFollowedByC() {
        IRule r = new FollowedBy(new Kleene("a"), new FollowedBy("b", "c"));

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateKleene());

        Collection<IEvent> expected = new ArrayList<>();
        expected.add(new Event("a", 1));
        expected.add(new Event("a", 3));
        expected.add(new Event("a", 4));
        expected.add(new Event("a", 6));
        expected.add(new Event("b", 7));
        expected.add(new Event("c", 9));

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldThrowRuntimeExceptionNonTerminatingRule() {
        _thrown.expect(RuntimeException.class);
        _thrown.expectMessage(StringContains.containsString("Non-terminating rule"));

        IRule r = new Kleene("a");

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateKleene());
    }


    @Test
    public void shouldThrowRuntimeExceptionNonTerminatingRule2() {
        _thrown.expect(RuntimeException.class);
        _thrown.expectMessage(StringContains.containsString("Non-terminating rule"));

        IRule r = new FollowedBy("a", new Kleene("b"));

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateKleene());
    }

    @Test
    public void shouldDetectAFollowedByBWithTimeConstraint() {
        IRule r = new FollowedBy("a", "b").setTimeConstraint(5);

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateFollowedBy());

        Collection<IEvent> expected = new ArrayList<>();
        expected.add(new Event("a", 1));
        expected.add(new Event("b", 5));

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldNotDetectAFollowedByBWithTimeConstraint() {
        IRule r = new FollowedBy("a", "b").setTimeConstraint(3);

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateFollowedBy());

        Collection<IEvent> expected = new ArrayList<>();

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldDetectASeqWithTimeConstraint() {
        IRule r = new FollowedBy(
                new Kleene("a").setTimeConstraint(5),
                "end");

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateKleene());

        Collection<IEvent> expected = new ArrayList<>();
        expected.add(new Event("a", 1));
        expected.add(new Event("a", 3));
        expected.add(new Event("a", 4));
        expected.add(new Event("a", 6));
        expected.add(new Event("a", 10));
        expected.add(new Event("end", 11));

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldNotDetectASeqWithTimeConstraint() {
        IRule r = new FollowedBy(
                new Kleene("a").setTimeConstraint(3),
                "end");

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateKleene());

        Collection<IEvent> expected = new ArrayList<>();

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldDetectAFollowedByBSeqWithTimeConstraint() {
        IRule r = new FollowedBy(
                new Kleene(new FollowedBy("a", "b")).setTimeConstraint(5),
                "end");

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateKleene2());

        Collection<IEvent> expected = new ArrayList<>();
        expected.add(new Event("a", 1));
        expected.add(new Event("b", 3));
        expected.add(new Event("a", 8));
        expected.add(new Event("b", 16));
        expected.add(new Event("end", 19));

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldNotDetectAFollowedByBSeqWithTimeConstraint() {
        IRule r = new FollowedBy(
                new Kleene(new FollowedBy("a", "b")).setTimeConstraint(4),
                "end");

        _ruleManager.addRule(r);
        _detector.detect(_generator.generateKleene2());

        Collection<IEvent> expected = new ArrayList<>();

        Collection<IEvent> actual = _ruleManager.getLastPattern();

        Assert.assertEquals(expected, actual);
    }
}
