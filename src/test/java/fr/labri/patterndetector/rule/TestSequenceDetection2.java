package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.executor.Event;
import fr.labri.patterndetector.executor.IEvent;
import fr.labri.patterndetector.rule.FollowedBy;
import fr.labri.patterndetector.rule.Kleene;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by morandat on 17/05/2016.
 */
@RunWith(Parameterized.class)
public class TestSequenceDetection2 extends AbstractTestDetection {

    public Stream<? extends IEvent> generate() {
        return Arrays.asList(
                new Event("x", 1),
                new Event("a", 2),
                new Event("c", 3),
                new Event("b", 4),
                new Event("x", 5),
                new Event("x", 6),
                new Event("x", 7),
                new Event("x", 8),
                new Event("a", 9),
                new Event("y", 10),
                new Event("y", 11),
                new Event("y", 12),
                new Event("y", 13),
                new Event("y", 14),
                new Event("y", 15),
                new Event("y", 16),
                new Event("b", 17),
                new Event("x", 18),
                new Event("c", 19),
                new Event("end", 20)
        ).stream();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection patterns() {
        return Arrays.asList(
                new Object[][]{
                        {
                                " Detect Kleene(A), with time constraint ",
                                new FollowedBy(new Kleene("a"), "end"), //TODO .setTimeConstraint(5)
                                Arrays.asList(
                                        new Event("a", 2),
                                        new Event("a", 4),
                                        new Event("a", 5),
                                        new Event("a", 7),
                                        new Event("a", 11),
                                        new Event("end", 12))
                        },

                        {
                                " NOT detect Kleene(A), with time constraint ",
                                new FollowedBy(new Kleene("a"), "end"), // TODO .setTimeConstraint(3)
                                new ArrayList<IEvent>()
                        },

                        {
                                " Detect Kleene(A followed by B), with time constraint ",
                                new FollowedBy(new Kleene(new FollowedBy("a", "b")), "end"), //TODO .setTimeConstraint(5),
                                Arrays.asList(
                                        new Event("a", 2),
                                        new Event("b", 4),
                                        new Event("a", 9),
                                        new Event("b", 17),
                                        new Event("end", 20))
                        },

                        {
                                " NOT detect Kleene(A followed by B), with time constraint ",
                                new FollowedBy(new Kleene(new FollowedBy("a", "b")), "end"),//TODO .setTimeConstraint(4),
                                new ArrayList<IEvent>()
                        },
                });
    }

}
