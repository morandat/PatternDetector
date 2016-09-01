package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.AutomatonRunnerType;
import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.Event;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

@RunWith(Parameterized.class)
public class TestFollowedByDetection extends AbstractTestDetection {

    public Stream<? extends Event> generate() {
        return Arrays.asList(
                new Event("b", 1),
                new Event("a", 2),
                new Event("x", 3),
                new Event("a", 4),
                new Event("c", 5),
                new Event("b", 6)
        ).stream();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection patterns() {
        return Arrays.asList(
                new Object[][]{
                        {
                                " Detect A followed by B ",
                                new FollowedBy("a", "b"),
                                Arrays.asList(
                                        new Event("a", 2),
                                        new Event("b", 6)),
                                AutomatonRunnerType.Deterministic
                        },

                        {
                                " Detect A followed by A ",
                                new FollowedBy("a", "a"),
                                Arrays.asList(
                                        new Event("a", 2),
                                        new Event("a", 4)),
                                AutomatonRunnerType.Deterministic
                        },

                        /*{
                                " Detect A followed by B, with time constraint ",
                                new FollowedBy("a", "b"), //TODO .setTimeConstraint(5);
                                Arrays.asList(
                                        new Event("a", 2),
                                        new Event("b", 6)),
                                AutomatonRunnerType.Deterministic
                        },

                        {
                                " NOT detect A followed by B, with time constraint ",
                                new FollowedBy("a", "b"), //TODO .setTimeConstraint(3);
                                new ArrayList<Event>(),
                                AutomatonRunnerType.Deterministic
                        },*/
                });
    }
}
