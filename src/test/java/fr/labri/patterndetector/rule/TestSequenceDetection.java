package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.AutomatonRunnerType;
import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.IEvent;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;


/**
 * Created by morandat on 17/05/2016.
 */
@RunWith(Parameterized.class)
public class TestSequenceDetection extends AbstractTestDetection {

    public Stream<? extends IEvent> generate() {
        return Arrays.asList(
                new Event("x", 1),
                new Event("a", 2).setData("x", 1),
                new Event("c", 3),
                new Event("a", 4).setData("x", 2),
                new Event("a", 5).setData("x", 3),
                new Event("y", 6),
                new Event("a", 7).setData("x", 4),
                new Event("b", 8),
                new Event("x", 9),
                new Event("c", 10),
                new Event("a", 11),
                new Event("end", 12)
        ).stream();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection patterns() {
        return Arrays.asList(
                new Object[][]{
                        {
                                " Detect Kleene(A) followed by B ",
                                new FollowedBy(new Kleene("a"), "b"),
                                Arrays.asList(
                                        new Event("a", 2),
                                        new Event("a", 4),
                                        new Event("a", 5),
                                        new Event("a", 7),
                                        new Event("b", 8)),
                                AutomatonRunnerType.Deterministic
                        },

                        {
                                " Detect Kleene(A) followed by B followed by C ",
                                new FollowedBy(new Kleene("a"), new FollowedBy("b", "c")),
                                Arrays.asList(
                                        new Event("a", 2),
                                        new Event("a", 4),
                                        new Event("a", 5),
                                        new Event("a", 7),
                                        new Event("b", 8),
                                        new Event("c", 10)),
                                AutomatonRunnerType.Deterministic
                        },
                });
    }
}
