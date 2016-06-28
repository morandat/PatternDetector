package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.rule.*;
import fr.labri.patterndetector.runtime.predicates.*;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by William Braik on 6/22/2015.
 */
public class Main {

    public static void main(String[] args) {

        IRule nacRule = new Atom("AddBasket")
                .addPredicate(new StringPredicateArity2(
                        new FieldAtom("0", "productId"),
                        new FieldKleeneStaticIndex("1", "productId", 0),
                        (x, y) -> x.getValue().equals(y.getValue())));

        // .addStartNacMarker(...); FIXME should not be allowed on a NAC rule, to prevent infinite nesting of NAC rules...

        IRule mainRule = new FollowedBy(
                new Kleene("View")
                        .addPredicate(new StringPredicateArity2(
                                new FieldKleeneDynamicIndex("1", "productId", i -> i),
                                new FieldKleeneDynamicIndex("1", "productId", i -> i - 1),
                                (x, y) -> x.getValue().equals(y.getValue())))
                        .addStartNacMarker(new StartNacMarker(nacRule, "nac")),
                new Atom("Exit")
                        .addStopNacMarker(new StopNacMarker("nac")));

        RuleManager ruleManager = new RuleManager();
        Detector detector = new Detector(ruleManager);
        ruleManager.addRule(mainRule, AutomatonRunnerType.Deterministic);
        detector.detect(Main.generate());
    }

    private static Stream<? extends IEvent> generate() {
        return Arrays.asList(
                new Event("View", 1)
                        .setData("productId", "sku"),
                new Event("Exit", 6)
        ).stream();
    }
}
