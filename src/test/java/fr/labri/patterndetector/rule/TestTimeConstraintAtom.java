package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.AutomatonRunnerType;
import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.predicates.Constant;
import fr.labri.patterndetector.runtime.predicates.FieldAtomTime;
import fr.labri.patterndetector.runtime.predicates.FieldCurrentTime;
import fr.labri.patterndetector.runtime.predicates.builtins.Add;
import fr.labri.patterndetector.runtime.predicates.builtins.GreaterThan;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

@RunWith(Parameterized.class)
public class TestTimeConstraintAtom extends AbstractTestDetection {

    public Stream<? extends Event> generate() {
        return scenario();
    }

    public static Stream<? extends Event> scenario() {
        return Arrays.asList(
                new Event("Search", 1)
                        .setData("url", "s")
                        .setData("referrer", "x"),
                new Event("Purchase", 6)
                        .setData("url", "p")
                        .setData("referrer", "s")
        ).stream();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection patterns() {
        return Arrays.asList(
                new Object[][]{
                        {
                                " Detect scenario : Search -> Purchase ",
                                new FollowedBy(
                                        new Atom("Search"),
                                        new Atom("Purchase")
                                                .addPredicate(new GreaterThan(
                                                        new Add(new FieldAtomTime(0), new Constant(5)),
                                                        new FieldCurrentTime()))),
                                Arrays.asList(
                                        new Event("Search", 1),
                                        new Event("Purchase", 6)),
                                AutomatonRunnerType.Deterministic
                        },

                        // FIXME for the scenario below, the automaton should RESET after 2 seconds (timeout)
                        {
                                " Detect scenario : Search -> Purchase ",
                                new FollowedBy(
                                        new Atom("Search"),
                                        new Atom("Purchase")
                                                .addPredicate(new GreaterThan(
                                                        new Add(new FieldAtomTime(0), new Constant(2)),
                                                        new FieldCurrentTime()))),
                                new ArrayList<>(),
                                AutomatonRunnerType.Deterministic
                        },
                });
    }
}
