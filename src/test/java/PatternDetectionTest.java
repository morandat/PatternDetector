import fr.labri.patterndetector.executor.*;
import fr.labri.patterndetector.rule.*;
import generators.*;
import org.junit.*;
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

    private RuleManager _ruleManager;
    private Detector _detector;

    private String _testName;
    private IRule _testRule;
    private Collection<IEvent> _expectedPattern;
    private IGenerator _generator;

    @Before
    public void initializeTest() {
        logger.info("## Executing test : " + _testName);

        _ruleManager.removeAllRules();
    }

    public PatternDetectionTest(String testName, IRule testRule, Collection<IEvent> expectedPattern, IGenerator generator) {
        _testName = testName;
        _testRule = testRule;
        _expectedPattern = expectedPattern;
        _generator = generator;

        _ruleManager = new RuleManager();
        _detector = new Detector(_ruleManager);
    }

    @Parameterized.Parameters(name = "{0}")
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
                                        new Atom("a"),//TODO .addPredicate("x", x -> x > 5);
                                        new ArrayList<IEvent>() {{
                                            new Event("a", 2);
                                        }},
                                        new AtomGenerator()
                                },

                                {
                                        " NOT detect A, with predicate ",
                                        new Atom("a"),//TODO .addPredicate("x", x -> x > 20);
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
                                        " Detect Kleene(A) followed by B ",
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
                                        " Detect Kleene(A) followed by B followed by C ",
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
                                        new FollowedBy("a", "b"), //TODO .setTimeConstraint(5);
                                        new ArrayList<IEvent>() {{
                                            add(new Event("a", 2));
                                            add(new Event("b", 6));
                                        }},
                                        new FollowedByGenerator()
                                },

                                {
                                        " NOT detect A followed by B, with time constraint ",
                                        new FollowedBy("a", "b"), //TODO .setTimeConstraint(3);
                                        new ArrayList<IEvent>(),
                                        new FollowedByGenerator()
                                },

                                {
                                        " Detect Kleene(A), with time constraint ",
                                        new FollowedBy(new Kleene("a"), "end"), //TODO .setTimeConstraint(5)
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
                                        new FollowedBy(new Kleene("a"), "end"), // TODO .setTimeConstraint(3)
                                        new ArrayList<IEvent>(),
                                        new KleeneGenerator2()
                                },

                                {
                                        " Detect Kleene(A followed by B), with time constraint ",
                                        new FollowedBy(new Kleene(new FollowedBy("a", "b")), "end"), //TODO .setTimeConstraint(5),
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
                                        new FollowedBy(new Kleene(new FollowedBy("a", "b")), "end"),//TODO .setTimeConstraint(4),
                                        new ArrayList<IEvent>(),
                                        new KleeneGenerator2()
                                },

                                {
                                        " Detect search scenario ",
                                        new FollowedBy("q", new FollowedBy(new Kleene("f"), "a")),
                                        new ArrayList<IEvent>() {{
                                            add(new Event("q", 1));
                                            add(new Event("f", 2));
                                            add(new Event("f", 3));
                                            add(new Event("a", 4));
                                        }},
                                        new SearchGenerator()
                                },
                        });
    }

    @Test
    public void patternDetectionTest() {
        String ruleName = _ruleManager.addRule(_testRule, DefaultAutomatonRunner.class);
        _ruleManager.getRunner(ruleName).registerPatternObserver(
                (Collection<IEvent> pattern) -> Assert.assertEquals(_expectedPattern, pattern));

        _detector.detect(_generator.generate());
    }
}
