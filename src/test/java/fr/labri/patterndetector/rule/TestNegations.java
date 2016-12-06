package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.AutomatonRunnerType;
import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.expressions.*;
import fr.labri.patterndetector.runtime.expressions.predicates.Equals;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

@RunWith(Parameterized.class)
public class TestNegations extends AbstractTestDetection {

    public Stream<? extends Event> generate() {
        return scenario();
    }

    public static Stream<? extends Event> scenario() {
        return Arrays.asList(
                new Event("View", 1)
                        .setData("productId", "sku"),
                new Event("View", 2)
                        .setData("productId", "sku2"),
                new Event("AddBasket", 4)
                        .setData("productId", "sku"),
                new Event("View", 5)
                        .setData("productId", "sku"),
                new Event("Exit", 6)
        ).stream();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection patterns() {
        return Arrays.asList(
                new Object[][]{
                        {
                                " Detect scenario : k+ -> end # end.t - k[0].t < 10 ",
                                new FollowedBy(
                                        new Kleene("View")
                                                .addPredicate(new Equals(
                                                        new FieldCurrent("productId"),
                                                        new FieldKleeneStaticIndex(1, "productId", -1)))
                                                .addNegationBeginMarker(new NegationBeginMarker(
                                                        // negation rule
                                                        new Atom("AddBasket")
                                                                .addPredicate(new Equals(
                                                                        new FieldAtom(0, "productId"),
                                                                        new FieldKleeneStaticIndex(1, "productId", 0))),
                                                        "negation")),
                                        new Atom("Exit")
                                                .addNegationEndMarker(new NegationEndMarker("negation"))),
                                Arrays.asList(
                                        new Event("View", 5),
                                        new Event("Exit", 6)),
                                AutomatonRunnerType.Deterministic
                        }
                });
    }
}
