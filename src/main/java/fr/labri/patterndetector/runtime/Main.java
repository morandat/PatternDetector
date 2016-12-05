package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.lang.Builtin;
import fr.labri.patterndetector.rule.*;
import fr.labri.patterndetector.runtime.expressions.*;
import fr.labri.patterndetector.runtime.expressions.predicates.Equal;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by William Braik on 6/22/2015.
 */
public class Main {

    public static void main(String[] args) {
        IRule nacRule = new Atom("AddBasket")
                .addPredicate(new Equal(
                        new FieldAtom(0, "productId"), new FieldKleeneStaticIndex(1, "productId", 0)));

        IRule mainRule = new FollowedBy(
                new Kleene("View")
                        .addPredicate(new Equal(
                                new FieldKleeneDynamicIndex(1, "productId", i -> i),
                                new FieldKleeneDynamicIndex(1, "productId", i -> i - 1)))
                        .addNacBeginMarker(new NacBeginMarker(nacRule, "nac")),
                new Atom("Exit")
                        .addNacEndMarker(new NacEndMarker("nac")));

        RuleManager ruleManager = new RuleManager();
        Detector detector = new Detector(ruleManager);
        ruleManager.addRule(mainRule, AutomatonRunnerType.NonDeterministicMatchFirst);
        detector.detect(Main.generate());

        IRule rule_not_working = new FollowedBy(
                new FollowedBy(
                        new Atom("SEARCH")
                                .setAction((Runnable & Serializable) () -> System.out.println("SEARCH ACTION TRIGGERED !")),

                        new Atom("PRODUCT_SHEET")
                                .addPredicate(new Equal(
                                        new FieldAtom(0, "url"),
                                        new FieldAtom(1, "ref")))
                                .setAction((Runnable & Serializable) () -> System.out.println("PRODUCT SHEET ACTION TRIGGERED !"))),

                new Atom("ADD_TO_BASKET")
                        .addPredicate(new Equal(
                                new FieldAtom(1, "url"),
                                new FieldAtom(2, "ref"))))

                .setName("basic");

        IRule rule = new FollowedBy(new Kleene("SEARCH"), "ADD_TO_BASKET");

        ruleManager = new RuleManager();
        ruleManager.addRule(rule, AutomatonRunnerType.Deterministic);
        detector = new Detector(ruleManager);
        detector.detect(Main.generate());
    }

    private static Stream<? extends Event> generate() {
        return Arrays.asList(
                new Event("SEARCH", 1)
                        .setData("url", "http://www.cdiscount.com/search/10/chaussure.html"),

                new Event("SEARCH", 2)
                        .setData("url", "http://www.cdiscount.com/search/10/chaussure.html"),

                new Event("PRODUCT_SHEET", 4)
                        .setData("url", "http://www.cdiscount.com/chaussures/baskets-enfant-chaussures-chaussures-de-sports-b/f-150-mp03218702.html")
                        .setData("ref", "http://www.cdiscount.com/search/10/chaussure.html")
                        .setData("price", 12.8),

                new Event("ADD_TO_BASKET", 7)
                        .setData("url", "http://www.cdiscount.com/chaussures/baskets-enfant-chaussures-chaussures-de-sports-b/r2-150-mp03218702-106520079.html")
                        .setData("ref", "http://www.cdiscount.com/chaussures/baskets-enfant-chaussures-chaussures-de-sports-b/f-150-mp03218702.html")
                        .setData("price", 12.8)
        ).stream();
    }
}
