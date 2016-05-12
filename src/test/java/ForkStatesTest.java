import fr.labri.patterndetector.executor.Detector;
import fr.labri.patterndetector.executor.ForkAutomatonRunner;
import fr.labri.patterndetector.executor.RuleManager;
import fr.labri.patterndetector.rule.FollowedBy;
import fr.labri.patterndetector.rule.IRule;
import fr.labri.patterndetector.rule.Kleene;
import generators.SearchGenerator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by will on 26/10/15.
 */
public class ForkStatesTest {

    private final Logger logger = LoggerFactory.getLogger(PatternDetectionTest.class);

    private RuleManager _ruleManager = new RuleManager();
    private Detector _detector = new Detector(_ruleManager);

    @Rule
    public TestName _testName = new TestName();

    @Before
    public void initializeTest() {
        logger.info("## Executing test : " + _testName.getMethodName());

        _ruleManager.removeAllRules();
    }

    @Test
    public void forkStatesTest() {
        IRule r = new FollowedBy("q", new FollowedBy(new Kleene("f"), "a"));

        String ruleName = _ruleManager.addRule(r, ForkAutomatonRunner.class);
        _detector.detect(new SearchGenerator().generate());

        System.out.println(_ruleManager.getAutomaton(ruleName));
        _ruleManager.getRunner(ruleName).getCurrentStates().forEach(System.out::println);
    }
}
