package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.executor.AutomatonRunnerType;
import fr.labri.patterndetector.executor.Event;
import fr.labri.patterndetector.executor.IEvent;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

@RunWith(Parameterized.class)
public class TestFollowedByDetection extends AbstractTestDetection {

    public Stream<? extends IEvent> generate() {
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
                                AutomatonRunnerType.DFA
                        },

                        {
                                " Detect A followed by A ",
                                new FollowedBy("a", "a"),
                                Arrays.asList(
                                        new Event("a", 2),
                                        new Event("a", 4)),
                                AutomatonRunnerType.DFA
                        },

                        /*{
                                " Detect A followed by B, with time constraint ",
                                new FollowedBy("a", "b"), //TODO .setTimeConstraint(5);
                                Arrays.asList(
                                        new Event("a", 2),
                                        new Event("b", 6)),
                                AutomatonRunnerType.DFA
                        },

                        {
                                " NOT detect A followed by B, with time constraint ",
                                new FollowedBy("a", "b"), //TODO .setTimeConstraint(3);
                                new ArrayList<IEvent>(),
                                AutomatonRunnerType.DFA
                        },*/
                });
    }
}