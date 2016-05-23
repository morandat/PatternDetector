import fr.labri.patterndetector.executor.*;
import fr.labri.patterndetector.rule.FollowedBy;
import fr.labri.patterndetector.rule.IRule;
import fr.labri.patterndetector.rule.Kleene;
import org.hamcrest.core.StringContains;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by will on 26/10/15.
 */
@RunWith(Parameterized.class)
public class ExceptionTest {

    @Parameterized.Parameter(0)
    public String _testName;
    @Parameterized.Parameter(1)
    public IRule _testRule;
    @Parameterized.Parameter(2)
    public Class<Exception> _expectedExceptionClass;
    @Parameterized.Parameter(3)
    public String _expectedExceptionMsg;

    @Rule
    public ExpectedException _thrown = ExpectedException.none();

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
                                },

                                {
                                        " Should throw non-terminating rule exception ",
                                        new FollowedBy("a", new Kleene("b")),
                                        RuntimeException.class,
                                        "Non-terminating rule",
                                },
                        });
    }

    @Test
    public void exceptionTest() {
        RuleManager ruleManager = new RuleManager();

        _thrown.expect(_expectedExceptionClass);
        _thrown.expectMessage(StringContains.containsString(_expectedExceptionMsg));

        ruleManager.addRule(_testRule, AutomatonRunnerType.Deterministic);
    }
}
