import fr.labri.patterndetector.executor.DefaultAutomatonRunner;
import fr.labri.patterndetector.executor.Detector;
import fr.labri.patterndetector.executor.RuleManager;
import fr.labri.patterndetector.rule.FollowedBy;
import fr.labri.patterndetector.rule.IRule;
import fr.labri.patterndetector.rule.Kleene;
import generators.IGenerator;
import generators.KleeneGenerator;
import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by will on 26/10/15.
 */
@RunWith(Parameterized.class)
public class ExceptionTest {

    private final Logger logger = LoggerFactory.getLogger(PatternDetectionTest.class);

    private RuleManager _ruleManager;
    private Detector _detector;

    private String _testName;
    private IRule _testRule;
    private Class<Exception> _expectedExceptionClass;
    private String _expectedExceptionMsg;
    private IGenerator _generator;

    @Rule
    public ExpectedException _thrown = ExpectedException.none();

    @Before
    public void initializeTest() {
        logger.info("## Executing test : " + _testName);

        _ruleManager.removeAllRules();
    }

    public ExceptionTest(String testName, IRule testRule, Class<Exception> expectedExceptionClass, String expectedExceptionMsg, IGenerator generator) {
        _testName = testName;
        _testRule = testRule;
        _expectedExceptionClass = expectedExceptionClass;
        _expectedExceptionMsg = expectedExceptionMsg;
        _generator = generator;

        _ruleManager = new RuleManager();
        _detector = new Detector(_ruleManager);
    }

    @Parameterized.Parameters
    public static Collection patterns() {
        return Arrays.asList(
                new Object[][]
                        {
                                {
                                        " Should throw non-terminating rule exception ",
                                        new Kleene("a"),
                                        RuntimeException.class,
                                        "Non-terminating rule",
                                        new KleeneGenerator()
                                },

                                {
                                        " Should throw non-terminating rule exception ",
                                        new FollowedBy("a", new Kleene("b")),
                                        RuntimeException.class,
                                        "Non-terminating rule",
                                        new KleeneGenerator()
                                },
                        });
    }

    @Test
    public void exceptionTest() {
        _thrown.expect(_expectedExceptionClass);
        _thrown.expectMessage(StringContains.containsString(_expectedExceptionMsg));

        _ruleManager.addRule(_testRule, DefaultAutomatonRunner.class);
        _detector.detect(_generator.generate());
    }
}
