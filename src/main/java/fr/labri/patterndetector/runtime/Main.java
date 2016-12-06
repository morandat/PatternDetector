package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.rule.*;
import fr.labri.patterndetector.runtime.expressions.*;
import fr.labri.patterndetector.runtime.expressions.predicates.Equals;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by William Braik on 6/22/2015.
 */
public class Main {

    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        RuleManager ruleManager = new RuleManager();
        Detector detector = new Detector(ruleManager);



        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        IRule negationRule = new Atom("AddBasket")
                .addPredicate(new Equals(
                        new FieldAtom(0, "productId"), new FieldKleeneStaticIndex(1, "productId", 0)));

        IRule mainRule = new FollowedBy(
                new Kleene("View")
                        .addPredicate(new Equals(
                                new FieldKleeneDynamicIndex(1, "productId", i -> i),
                                new FieldKleeneDynamicIndex(1, "productId", i -> i - 1)))
                        .addNegationBeginMarker(new NegationBeginMarker(negationRule, "negation")),
                new Atom("Exit")
                        .addNegationEndMarker(new NegationEndMarker("negation")));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        IRule rule = new FollowedBy(new Kleene("SEARCH"), "ADD_TO_BASKET");

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        IRule rule_not_working =
                new FollowedBy(
                        new FollowedBy(
                                new Atom("SEARCH"),
                                new Atom("PRODUCT_SHEET")
                                        .addPredicate(new Equals(
                                                new FieldAtom(0, "url"),
                                                new FieldAtom(1, "ref")))),

                        new Atom("ADD_TO_BASKET")
                                .addPredicate(new Equals(
                                        new FieldAtom(1, "url"),
                                        new FieldAtom(2, "ref"))));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        IAutomatonRunner runner = ruleManager.addRule(rule_not_working, AutomatonRunnerType.Deterministic);

        /*try { // FIXME ad-hoc code for spark automaton import. Uncomment to export automaton
            Main.serializeAutomaton(runner.getAutomaton(), "D:\\automaton.ser");
        } catch (IOException e) {
            throw new RuntimeException("Automaton could not be serialized : " + e.getMessage());
        }*/

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

    private static void serializeAutomaton(IRuleAutomaton automaton, String file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(automaton);
        Logger.debug("Automaton serialized");
    }
}
