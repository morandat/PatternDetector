import fr.labri.patterndetector.executor.*;
import fr.labri.patterndetector.rule.*;
import generators.*;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class PatternDetectionTest {

    private final Logger logger = LoggerFactory.getLogger(PatternDetectionTest.class);

    private RuleManager _ruleManager = new RuleManager();
    private Detector _detector = new Detector(_ruleManager);

    private String _testName;
    private IRule _testRule;
    private Collection<IEvent> _expectedPattern;
    private IGenerator _generator;

    @Rule
    public TestName _name = new TestName();

    @Before
    public void initializeTest() {
        logger.info("## Executing test : " + _name.getMethodName());

        _ruleManager.removeAllRules();
    }

    public PatternDetectionTest(String testName, IRule testRule, Collection<IEvent> expectedPattern, IGenerator generator) {
        _testName = testName;
        _testRule = testRule;
        _expectedPattern = expectedPattern;
        _generator = generator;
    }

    @Parameterized.Parameters
    public static Collection patterns() {
        return Arrays.asList(
                new Object[][]
                        {
                                {
                                        " Detect A ",
                                        new Atom("a"),
                                        new ArrayList<IEvent>() {{
                                            new Event("a", 2);
                                        }},
                                        new AtomGenerator()
                                },

                                {
                                        " Detect A, with predicate ",
                                        new Atom("a"),//.addPredicate("x", x -> x > 5);
                                        new ArrayList<IEvent>() {{
                                            new Event("a", 2);
                                        }},
                                        new AtomGenerator()
                                },

                                {
                                        " NOT detect A, with predicate ",
                                        new Atom("a"),//.addPredicate("x", x -> x > 20);
                                        new ArrayList<IEvent>(),
                                        new AtomGenerator()
                                },

                                {
                                        " Detect A or B ",
                                        new Or("a", "b"),
                                        new ArrayList<IEvent>() {{
                                            new Event("b", 3);
                                        }},
                                        new OrGenerator()
                                },

                                {
                                        " Detect A or B or C or D ",
                                        new Or(new Or("a", "b"), new Or("c", "d")),
                                        new ArrayList<IEvent>() {{
                                            new Event("c", 4);
                                        }},
                                        new OrGenerator()
                                },

                                {
                                        " Detect A followed by B, or A followed by C ",
                                        new Or(new FollowedBy("a", "b"), new FollowedBy("a", "c")),
                                        new ArrayList<IEvent>() {{
                                            add(new Event("a", 1));
                                            add(new Event("b", 3));
                                        }},
                                        new OrGenerator()
                                },

                                {
                                        " Detect Kleene(A) followed by B, or A followed by B ",
                                        new Or(new FollowedBy(new Kleene("a"), "b"), new FollowedBy("a", "b")),
                                        new ArrayList<IEvent>() {{
                                            add(new Event("a", 1));
                                            add(new Event("a", 2));
                                            add(new Event("b", 3));
                                        }},
                                        new OrGenerator()
                                },

                                {
                                        " Detect Kleene(A) followed by B, or A followed by B ",
                                        new Or(new FollowedBy(new Kleene("a"), "b"), new FollowedBy("a", "b")),
                                        new ArrayList<IEvent>() {{
                                            add(new Event("a", 1));
                                            add(new Event("a", 2));
                                            add(new Event("b", 3));
                                        }},
                                        new OrGenerator()
                                },

                                {
                                        " Detect A followed by B ",
                                        new FollowedBy("a", "b"),
                                        new ArrayList<IEvent>() {{
                                            add(new Event("a", 2));
                                            add(new Event("b", 6));
                                        }},
                                        new FollowedByGenerator()
                                },

                                {
                                        " Detect A followed by A ",
                                        new FollowedBy("a", "a"),
                                        new ArrayList<IEvent>() {{
                                            add(new Event("a", 2));
                                            add(new Event("a", 4));
                                        }},
                                        new FollowedByGenerator()
                                },

                                {
                                        " Detect A followed by A ",
                                        new FollowedBy(new Kleene("a"), "b"),
                                        new ArrayList<IEvent>() {{
                                            add(new Event("a", 2));
                                            add(new Event("a", 4));
                                            add(new Event("a", 5));
                                            add(new Event("a", 7));
                                            add(new Event("b", 8));
                                        }},
                                        new KleeneGenerator()
                                },

                                {
                                        " Detect Kleene(A) followed by B, followed by C ",
                                        new FollowedBy(new Kleene("a"), new FollowedBy("b", "c")),
                                        new ArrayList<IEvent>() {{
                                            add(new Event("a", 2));
                                            add(new Event("a", 4));
                                            add(new Event("a", 5));
                                            add(new Event("a", 7));
                                            add(new Event("b", 8));
                                            add(new Event("c", 10));
                                        }},
                                        new KleeneGenerator()
                                },

                                {
                                        " Detect A followed by B, with time constraint ",
                                        new FollowedBy("a", "b"), //.setTimeConstraint(5);
                                        new ArrayList<IEvent>() {{
                                            add(new Event("a", 2));
                                            add(new Event("b", 6));
                                        }},
                                        new FollowedByGenerator()
                                },

                                {
                                        " NOT detect A followed by B, with time constraint ",
                                        new FollowedBy("a", "b"), //.setTimeConstraint(3);
                                        new ArrayList<IEvent>(),
                                        new FollowedByGenerator()
                                },

                                {
                                        " Detect Kleene(A), with time constraint ",
                                        new FollowedBy(new Kleene("a"), "end"),//.setTimeConstraint(5)
                                        new ArrayList<IEvent>() {{
                                            add(new Event("a", 2));
                                            add(new Event("a", 4));
                                            add(new Event("a", 5));
                                            add(new Event("a", 7));
                                            add(new Event("a", 11));
                                            add(new Event("end", 12));
                                        }},
                                        new KleeneGenerator2()
                                },

                                {
                                        " NOT detect Kleene(A), with time constraint ",
                                        new FollowedBy(new Kleene("a"), "end"),//.setTimeConstraint(3)
                                        new ArrayList<IEvent>(),
                                        new KleeneGenerator2()
                                },

                                {
                                        " Detect Kleene(A followed by B), with time constraint ",
                                        new FollowedBy(new Kleene(new FollowedBy("a", "b")), "end"),//.setTimeConstraint(5),
                                        new ArrayList<IEvent>() {{
                                            add(new Event("a", 2));
                                            add(new Event("b", 4));
                                            add(new Event("a", 9));
                                            add(new Event("b", 17));
                                            add(new Event("end", 20));
                                        }},
                                        new KleeneGenerator2()
                                },

                                {
                                        " NOT detect Kleene(A followed by B), with time constraint ",
                                        new FollowedBy(new Kleene(new FollowedBy("a", "b")), "end"),//.setTimeConstraint(4),
                                        new ArrayList<IEvent>(),
                                        new KleeneGenerator2()
                                },

                                {
                                        " NOT detect search scenario ",
                                        new FollowedBy("q", new FollowedBy(new Kleene("f"), "a")),
                                        new ArrayList<IEvent>() {{
                                            add(new Event("q", 0));
                                            add(new Event("f", 1));
                                            add(new Event("f", 2));
                                            add(new Event("a", 3));
                                        }},
                                        new SearchGenerator()
                                },
                        });
    }

    @Test
    public void patternDetectionTest() {
        _ruleManager.addRule(_testRule);
        _detector.detect(_generator.generate());
        Collection<IEvent> actualPattern = _ruleManager.getLastPattern();
        Assert.assertEquals(_expectedPattern, actualPattern);
    }
}
