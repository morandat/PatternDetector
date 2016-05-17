package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.executor.Event;
import fr.labri.patterndetector.executor.IEvent;
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
                new Event("f", 2).setData("url", "f1").setData("ref", "q"),
                new Event("f", 3).setData("url", "f2").setData("ref", "q"),
                new Event("a", 4).setData("url", "a").setData("ref", "f2")).stream();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection patterns() {
        return Arrays.asList(
                new Object[][]{
                        {
                                " Detect search scenario ",
                                new FollowedBy("q", new FollowedBy(new Kleene("f"), "a")),
                                Arrays.asList(
                                        new Event("q", 1),
                                        new Event("f", 2),
                                        new Event("f", 3),
                                        new Event("a", 4))
                        }
                });
    }

}
