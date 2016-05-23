package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.executor.AutomatonRunnerType;
import fr.labri.patterndetector.executor.Event;
import fr.labri.patterndetector.executor.IEvent;
import fr.labri.patterndetector.executor.predicates.IntPredicateArity1;
import fr.labri.patterndetector.executor.predicates.IntPredicateArity2;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;


@RunWith(Parameterized.class)
public class TestPredicatesDetection extends AbstractTestDetection {

    public Stream<? extends IEvent> generate() {
        return Arrays.asList(
                new Event("a", 1).setData("x", 10),
                new Event("b", 2).setData("y", 15),
                new Event("b", 3).setData("y", 10)
        ).stream();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection patterns() {
        return Arrays.asList(
                new Object[][]{
                        {
                                "Detect A, with simple predicate",
                                new Atom("a")
                                        .addPredicate(new IntPredicateArity1("$0.x", x -> x.getValue() > 5)),
                                Arrays.asList(
                                        new Event("a", 1)),
                                AutomatonRunnerType.Deterministic
                        },
                        {
                                "NOT Detect A, with simple predicate",
                                new Atom("a")
                                        .addPredicate(new IntPredicateArity1("$0.x", x -> x.getValue() > 12)),
                                new ArrayList<>(),
                                AutomatonRunnerType.Deterministic
                        },
                        {
                                "Detect A followed by B, with complex predicate",
                                new FollowedBy(
                                        "a",
                                        new Atom("b")
                                                .addPredicate(new IntPredicateArity2("$0.x", "$1.y", (x, y) -> x.getValue() == y.getValue()))),
                                Arrays.asList(
                                        new Event("a", 1),
                                        new Event("b", 3)),
                                AutomatonRunnerType.Deterministic
                        },
                        {
                                "Detect A followed by B, with complex predicate",
                                new FollowedBy(
                                        "a",
                                        new Atom("b")
                                                .addPredicate(new IntPredicateArity2("$0.x", "$1.y", (x, y) -> x.getValue() != y.getValue()))),
                                Arrays.asList(
                                        new Event("a", 1),
                                        new Event("b", 2)),
                                AutomatonRunnerType.Deterministic
                        },
                });
    }
}
