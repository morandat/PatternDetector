package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.AutomatonRunnerType;
import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.predicates.FieldAtom;
import fr.labri.patterndetector.runtime.predicates.StringPredicateArity2;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

@RunWith(Parameterized.class)
public class TestScenario3Detection extends AbstractTestDetection {

    public Stream<? extends Event> generate() {
        return scenario();
    }

    public static Stream<? extends Event> scenario() {
        return Arrays.asList(
                new Event("PayOpts", 1)
                        .setData("url", "p")
                        .setData("referrer", "x"),
                new Event("RemoveBasket", 2)
                        .setData("url", "r")
                        .setData("referrer", "p"))
                .stream();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection patterns() {
        return Arrays.asList(
                new Object[][]{
                        {
                                " Detect scenario : PayOpts -> RemoveBasket ",
                                new FollowedBy(
                                        new Atom("PayOpts"),
                                        new Atom("RemoveBasket")
                                                .addPredicate(new StringPredicateArity2(
                                                        new FieldAtom("0", "url"),
                                                        new FieldAtom("1", "referrer"),
                                                        (x, y) -> x.getValue().equals(y.getValue())))),
                                Arrays.asList(
                                        new Event("PayOpts", 1),
                                        new Event("RemoveBasket", 2)),
                                AutomatonRunnerType.Deterministic
                        }
                });
    }
}
