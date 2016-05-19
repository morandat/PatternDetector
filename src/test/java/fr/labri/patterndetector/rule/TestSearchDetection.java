package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.executor.AutomatonRunnerType;
import fr.labri.patterndetector.executor.Event;
import fr.labri.patterndetector.executor.IEvent;
import fr.labri.patterndetector.executor.predicates.StringPredicateArity2;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

@RunWith(Parameterized.class)
public class TestSearchDetection extends AbstractTestDetection {

    public Stream<? extends IEvent> generate() {
        return searchScenario();
    }

    public static Stream<? extends IEvent> searchScenario() {
        return Arrays.asList(
                new Event("q", 1).setData("url", "q").setData("ref", "cdiscount.com"),
                new Event("x", 2).setData("irrelevant", "data"),
                new Event("f", 3).setData("url", "f").setData("ref", "q"),
                new Event("y", 4).setData("irrelevant", "data"),
                new Event("a", 5).setData("url", "a").setData("ref", "f")).stream();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection patterns() {
        return Arrays.asList(
                new Object[][]{
                        {
                                " Detect search scenario ",
                                new FollowedBy("q", new FollowedBy(
                                        new Atom("f").addPredicate(new StringPredicateArity2("$0.url", "$1.ref", (x, y) -> x.getValue().equals(y.getValue()))),
                                        new Atom("a").addPredicate(new StringPredicateArity2("$1.url", "$2.ref", (x, y) -> x.getValue().equals(y.getValue()))))),
                                Arrays.asList(
                                        new Event("q", 1),
                                        new Event("f", 3),
                                        new Event("a", 5)),
                                AutomatonRunnerType.DFA
                        }
                });
    }
}
