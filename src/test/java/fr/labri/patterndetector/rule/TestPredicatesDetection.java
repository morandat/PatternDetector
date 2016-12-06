package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.AutomatonRunnerType;
import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.expressions.*;
import fr.labri.patterndetector.runtime.expressions.builtins.Add;
import fr.labri.patterndetector.runtime.expressions.builtins.Equals;
import fr.labri.patterndetector.runtime.expressions.builtins.GreaterThan;
import fr.labri.patterndetector.runtime.expressions.builtins.NotEqual;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

@RunWith(Parameterized.class)
public class TestPredicatesDetection extends AbstractTestDetection {

    public Stream<? extends Event> generate() {
        return Arrays.asList(
                new Event("a", 1).setData("x", 10),
                new Event("b", 2).setData("y", 15),
                new Event("b", 3).setData("x", 10),
                new Event("b", 4).setData("x", 10),
                new Event("b", 5).setData("y", 10),
                new Event("b", 6).setData("y", 15),
                new Event("c", 7).setData("y", 15),
                new Event("c", 8).setData("y", 14)
        ).stream();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection patterns() {
        return Arrays.asList(
                new Object[][]{
                        {
                                "Detect A, with simple predicate",
                                new Atom("a")
                                        .addPredicate(new GreaterThan(
                                            FieldAccess.current().named("x"),
                                            Constant.from(5))),
                                Arrays.asList(
                                        new Event("a", 1)),
                                AutomatonRunnerType.Deterministic
                        },

                        {
                                "NOT Detect A, with simple predicate",
                                new Atom("a")
                                        .addPredicate(new GreaterThan(
                                        FieldAccess.current().named("x"),
                                        Constant.from(12))),
                                new ArrayList<>(),
                                AutomatonRunnerType.Deterministic
                        },

                        {
                                "Detect A followed by B, with == predicate",
                                new FollowedBy(
                                        "a",
                                        new Atom("b")
                                                .addPredicate(new Equals(
                                                        FieldAccess.byPosition(0).named("x"),
                                                        FieldAccess.current().named("y")))),
                                Arrays.asList(
                                        new Event("a", 1),
                                        new Event("b", 5)),
                                AutomatonRunnerType.Deterministic
                        },

                        {
                                "Detect A followed by B, with != predicate",
                                new FollowedBy(
                                        "a",
                                        new Atom("b")
                                                .addPredicate(new NotEqual(
                                                        FieldAccess.byPosition(0).named("x"),
                                                        FieldAccess.current().named("y")))),
                                Arrays.asList(
                                        new Event("a", 1),
                                        new Event("b", 2)),
                                AutomatonRunnerType.Deterministic
                        },

                        {
                                " Detect A followed by KleeneAccess(B) followed by C # a.x = b[i].x ",
                                new FollowedBy("a",
                                        new FollowedBy(
                                                new Kleene("b")
                                                        .addPredicate(new Equals(
                                                                FieldAccess.byPosition(0).named("x"),
                                                                FieldAccess.current().named("x"))),
                                                "c")),
                                Arrays.asList(
                                        new Event("a", 1),
                                        new Event("b", 3),
                                        new Event("b", 4),
                                        new Event("c", 7)),
                                AutomatonRunnerType.Deterministic
                        },

                        {
                                " Detect A followed by KleeneAccess(B) followed by C # b[] ",
                                new FollowedBy("a",
                                        new FollowedBy(
                                                new Kleene("b"),
                                                new Atom("c")
                                                        .addPredicate(new Equals(
                                                                new Add(
                                                                        FieldAccess.byStaticIndex(1, 0).named("y"),
                                                                        Constant.from(1)),
                                                                FieldAccess.current().named("y"))))),
                                Arrays.asList(
                                        new Event("a", 1),
                                        new Event("b", 2),
                                        new Event("b", 3),
                                        new Event("b", 4),
                                        new Event("b", 5),
                                        new Event("b", 6),
                                        new Event("c", 8)),
                                AutomatonRunnerType.Deterministic
                        },

                        {
                                " Detect KleeneAccess(B) followed by C with KleeneAccess predicates ",
                                new FollowedBy(
                                        new Kleene("b")
                                                .addPredicate(new Equals(
                                                        FieldAccess.current().named("x"),
                                                        FieldAccess.byStaticIndex(0, -2).named("x"))),
                                        "c"),
                                Arrays.asList(
                                        new Event("b", 3),
                                        new Event("b", 4),
                                        new Event("c", 7)),
                                AutomatonRunnerType.Deterministic
                        },

                        {
                                " Detect KleeneAccess(B) followed by C with KleeneAccess # b[i].y = b[i - 1].y",
                                new FollowedBy(
                                        new Kleene("b")
                                                .addPredicate(new Equals(
                                                        FieldAccess.current().named("y"),
                                                        FieldAccess.byStaticIndex(0, -2).named("y"))),
                                        "c"),
                                Arrays.asList(
                                        new Event("b", 2),
                                        new Event("b", 6),
                                        new Event("c", 7)),
                                AutomatonRunnerType.Deterministic
                        },
                });
    }
}
