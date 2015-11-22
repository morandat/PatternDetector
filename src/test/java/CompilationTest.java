import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.RuleAutomaton;
import fr.labri.patterndetector.compiler.RuleCompiler;
import fr.labri.patterndetector.executor.Event;
import fr.labri.patterndetector.executor.IEvent;
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
 * Created by Ma on 11/22/2015.
 */
public class CompilationTest {

    private final Logger logger = LoggerFactory.getLogger(CompilationTest.class);

    @Rule
    public TestName _name = new TestName();

    @Before
    public void initializeTest() {
        logger.info("## Executing test : " + _name.getMethodName());
    }

    @Test
    public void shouldCompileAtom() {
        IRule r = new Atom("a");

        IRuleAutomaton actual = new RuleCompiler().compile(r);

        IRuleAutomaton expected = new RuleAutomaton();
        // TODO test powerset or without powerset ? IMO without powerset. Maybe do a separate test for powerset

        Assert.assertEquals(expected, actual); // TODO might have to write equals() for RuleAutomaton
    }

    // TODO write tests for these
    /*IRule r = new FollowedBy("a", "b");
    //IRule r = new Kleene("a");
    //IRule r = new FollowedBy(new Kleene("a"), "b");
    IRule r = new FollowedBy(new Kleene(new FollowedBy("x", "y")), new FollowedBy("b", new Kleene("c")));
    */


}
