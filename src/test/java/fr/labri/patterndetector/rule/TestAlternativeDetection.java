package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.executor.AutomatonRunnerType;
import fr.labri.patterndetector.executor.Event;
import fr.labri.patterndetector.executor.IEvent;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;


@RunWith(Parameterized.class)
public class TestAlternativeDetection extends AbstractTestDetection {

    public Stream<? extends IEvent> generate() {
        return Arrays.asList(
                new Event("a", 1),
                new Event("a", 2),
                new Event("b", 3),
                new Event("c", 4)
        ).stream();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection patterns() {
        return Arrays.asList(
            new Object[][]{
                    {
                            "Detect A followed by B, or A followed by C",
                            new Or(new FollowedBy("a", "b"), new FollowedBy("a", "c")),
                            Arrays.asList(
                                    new Event("a", 1),
                                    new Event("b", 3)),
                            AutomatonRunnerType.Deterministic
                    },

                    {
                            "Detect A or B",
                            new Or("a", "b"),
                            Arrays.asList(
                                    new Event("a", 1)),
                            AutomatonRunnerType.Deterministic
                    },

                    {
                            " Detect A or B or C or D ",
                            new Or(new Or("a", "b"), new Or("c", "d")),
                            Arrays.asList(
                                    new Event("a", 1)),
                            AutomatonRunnerType.Deterministic
                    },

                    {
                            " Detect A followed by B, or A followed by C ",
                            new Or(new FollowedBy("a", "b"), new FollowedBy("a", "c")),
                            Arrays.asList(
                                    new Event("a", 1),
                                    new Event("b", 3)),
                            AutomatonRunnerType.Deterministic
                    },

                    {
                            " Detect Kleene(A) followed by B, or A followed by B ",
                            new Or(new FollowedBy(new Kleene("a"), "b"), new FollowedBy("a", "b")),
                            Arrays.asList(
                                    new Event("a", 1),
                                    new Event("a", 2),
                                    new Event("b", 3)),
                            AutomatonRunnerType.Deterministic
                    },
            });
       }
}
