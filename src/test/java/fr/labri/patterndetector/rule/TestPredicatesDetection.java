package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.AutomatonRunnerType;
import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.expressions.*;
import fr.labri.patterndetector.runtime.expressions.arithmetic.Add;
import fr.labri.patterndetector.runtime.expressions.predicates.Equal;
import fr.labri.patterndetector.runtime.expressions.predicates.GreaterThan;
import fr.labri.patterndetector.runtime.expressions.predicates.NotEqual;
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
                                        new FieldCurrent("x"),
                                        new Constant(5))),
                                Arrays.asList(
                                        new Event("a", 1)),
                                AutomatonRunnerType.Deterministic
                        },

                        {
                                "NOT Detect A, with simple predicate",
                                new Atom("a")
                                        .addPredicate(new GreaterThan(
                                        new FieldCurrent("x"),
                                        new Constant(12))),
                                new ArrayList<>(),
                                AutomatonRunnerType.Deterministic
                        },

                        {
                                "Detect A followed by B, with == predicate",
                                new FollowedBy(
                                        "a",
                                        new Atom("b")
                                                .addPredicate(new Equal(
                                                        new FieldAtom(0, "x"),
                                                        new FieldCurrent("y")))),
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
                                                        new FieldAtom(0, "x"),
                                                        new FieldCurrent("y")))),
                                Arrays.asList(
                                        new Event("a", 1),
                                        new Event("b", 2)),
                                AutomatonRunnerType.Deterministic
                        },

                        {
                                " Detect A followed by Kleene(B) followed by C # a.x = b[i].x ",
                                new FollowedBy("a",
                                        new FollowedBy(
                                                new Kleene("b")
                                                        .addPredicate(new Equal(
                                                                new FieldAtom(0, "x"),
                                                                new FieldCurrent("x"))),
                                                "c")),
                                Arrays.asList(
                                        new Event("a", 1),
                                        new Event("b", 3),
                                        new Event("b", 4),
                                        new Event("c", 7)),
                                AutomatonRunnerType.Deterministic
                        },

                        {
                                " Detect A followed by Kleene(B) followed by C # b[] ",
                                new FollowedBy("a",
                                        new FollowedBy(
                                                new Kleene("b"),
                                                new Atom("c")
                                                        .addPredicate(new Equal(
                                                                new Add(new FieldKleeneStaticIndex(1, "y", 0), new Constant(1)),
                                                                new FieldCurrent("y"))))),
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
                                " Detect Kleene(B) followed by C with Kleene predicates ",
                                new FollowedBy(
                                        new Kleene("b")
                                                .addPredicate(new Equal(
                                                        new FieldCurrent("x"),
                                                        new FieldKleeneStaticIndex(0, "x", -1))),
                                        "c"),
                                Arrays.asList(
                                        new Event("b", 3),
                                        new Event("b", 4),
                                        new Event("c", 7)),
                                AutomatonRunnerType.Deterministic
                        },

                        {
                                " Detect Kleene(B) followed by C with Kleene # b[i].y = b[i - 1].y",
                                new FollowedBy(
                                        new Kleene("b")
                                                .addPredicate(new Equal(
                                                        new FieldCurrent("y"),
                                                        new FieldKleeneStaticIndex(0, "y", -1))),
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
