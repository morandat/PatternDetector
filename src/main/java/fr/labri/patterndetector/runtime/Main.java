package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.rule.*;
import fr.labri.patterndetector.runtime.predicates.*;
import fr.labri.patterndetector.runtime.types.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

/**
 * Created by William Braik on 6/22/2015.
 */
public class Main {

    public static void main(String[] args) {
        IRule nacRule = new Atom("AddBasket")
                .addPredicate(new Predicate2(
                        new FieldAtom("productId", 0),
                        new FieldKleeneStaticIndex("productId", 1, 0)) {
                    @Override
                    public boolean evaluate(String first, String second) {
                        return first.equals(second);
                    }
                });

        IRule mainRule = new FollowedBy(
                new Kleene("View")
                        .addPredicate(new Predicate2(
                                new FieldKleeneDynamicIndex("productId", 1, i -> i),
                                new FieldKleeneDynamicIndex("productId", 1, i -> i - 1)) {
                            @Override
                            public boolean evaluate(String first, String second) {
                                return first.equals(second);
                            }
                        })
                        .addNacBeginMarker(new NacBeginMarker(nacRule, "nac")),
                new Atom("Exit")
                        .addNacEndMarker(new NacEndMarker("nac")));

        RuleManager ruleManager = new RuleManager();
        Detector detector = new Detector(ruleManager);
        ruleManager.addRule(mainRule, AutomatonRunnerType.NonDeterministicMatchFirst);
        detector.detect(Main.generate());

        IRule rule = new FollowedBy(
                new FollowedBy(
                        new Atom("SEARCH")
                                .setAction((Runnable & Serializable) () -> System.out.println("SEARCH ACTION TRIGGERED !")),

                        new Atom("PRODUCT_SHEET")
                                .addPredicate(new Predicate2(
                                        new FieldAtom("url", 0),
                                        new FieldAtom("ref", 1)) {
                                    @Override
                                    public boolean evaluate(String first, String second) {
                                        return first.equals(second);
                                    }
                                })
                                .setAction((Runnable & Serializable) () -> System.out.println("PRODUCT SHEET ACTION TRIGGERED !"))),

                new Atom("ADD_TO_BASKET")
                        .addPredicate(new Predicate2(
                                new FieldAtom("url", 1),
                                new FieldAtom("ref", 2)) {
                            @Override
                            public boolean evaluate(String first, String second) {
                                return first.equals(second);
                            }
                        }))

                .setName("basic");

        rule = new FollowedBy(new Kleene("SEARCH"), "ADD_TO_BASKET");

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
