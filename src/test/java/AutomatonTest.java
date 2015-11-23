import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.RuleAutomaton;
import fr.labri.patterndetector.compiler.RuleCompiler;
import fr.labri.patterndetector.rules.Atom;
import fr.labri.patterndetector.rules.IRule;
import org.hamcrest.core.StringContains;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wbraik on 11/22/2015.
 */
public class AutomatonTest {

    private final Logger logger = LoggerFactory.getLogger(AutomatonTest.class);

    @Rule
    public TestName _name = new TestName();

    @Rule
    public ExpectedException _thrown = ExpectedException.none();

    @Before
    public void initializeTest() {
        logger.info("## Executing test : " + _name.getMethodName());
    }

    @Ignore
    @Test
    public void shouldCompileAtom() {
        IRule r = new Atom("a");

        IRuleAutomaton actual = new RuleCompiler().compile(r);

        IRuleAutomaton expected = new RuleAutomaton();

        Assert.assertEquals(expected, actual); // TODO might have to write equals() for RuleAutomaton
    }

    // TODO write tests for these
    /*IRule r = new FollowedBy("a", "b");
    //IRule r = new Kleene("a");
    //IRule r = new FollowedBy(new Kleene("a"), "b");
    IRule r = new FollowedBy(new Kleene(new FollowedBy("x", "y")), new FollowedBy("b", new Kleene("c")));*/

    @Ignore
    public void shouldThrowRuntimeExceptionCompilationFailed() {
        _thrown.expect(RuntimeException.class);
        _thrown.expectMessage(StringContains.containsString("Compilation failed"));


    }

    @Ignore
    public void shouldComputePowerset() {

    }
}
