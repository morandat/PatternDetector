import fr.labri.patterndetector.Event;
import fr.labri.patterndetector.Generator;
import fr.labri.patterndetector.IEvent;
import fr.labri.patterndetector.RuleManager;
import fr.labri.patterndetector.rules.Atom;
import fr.labri.patterndetector.rules.FollowedBy;
import fr.labri.patterndetector.rules.IRule;
import fr.labri.patterndetector.rules.Kleene;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by william.braik on 14/08/2015.
 */
public class PatternDetectionTest {

    @Rule
    public TestName _name = new TestName();

    @Before
    public void initializeRuleManager() {
        System.out.println("*** Executing Test : " + _name.getMethodName() + " ***");
        RuleManager ruleManager = RuleManager.getInstance();
        ruleManager.removeAllRules();
        ruleManager.getPatternHistory().clear();
    }

    @Test
    public void testDetect1() {
        IRule r = new Atom("a");

        RuleManager ruleManager = RuleManager.getInstance();
        ruleManager.addRule(r);
        ruleManager.detect(Generator.generateStuff());

        Collection<IEvent> expected = new ArrayList<>();
        expected.add(new Event("a", 1));
        expected.add(new Event("a", 2));
        expected.add(new Event("a", 3));
        expected.add(new Event("a", 4));
        expected.add(new Event("a", 5));

        Collection<IEvent> actual = ruleManager.getPatternHistory();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDetect2() {
        IRule r = new FollowedBy(new FollowedBy(new Kleene("a"), "b"), "c");

        RuleManager ruleManager = RuleManager.getInstance();
        ruleManager.addRule(r);
        ruleManager.detect(Generator.generateStuff());

        Collection<IEvent> expected = new ArrayList<>();
        expected.add(new Event("a", 1));
        expected.add(new Event("a", 2));
        expected.add(new Event("a", 3));
        expected.add(new Event("a", 4));
        expected.add(new Event("a", 5));
        expected.add(new Event("b", 9));
        expected.add(new Event("c", 11));


        Collection<IEvent> actual = ruleManager.getPatternHistory();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRuleAmbiguity() {
        // TODO check ambiguous rules (test that checking unreachable transitions works)
        Assert.assertTrue(true);
    }

    // TODO tests : complex patterns, time constraints parentheses,
}
