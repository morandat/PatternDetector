package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.AutomatonRunnerType;
import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.IEvent;
import fr.labri.patterndetector.runtime.predicates.FieldAtom;
import fr.labri.patterndetector.runtime.predicates.LongPredicateArity1;
import fr.labri.patterndetector.runtime.predicates.StringPredicateArity1;
import fr.labri.patterndetector.runtime.predicates.StringPredicateArity2;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

@RunWith(Parameterized.class)
public class TestScenario2Detection extends AbstractTestDetection {

    public Stream<? extends IEvent> generate() {
        return scenario();
    }

    public static Stream<? extends IEvent> scenario() {
        return Arrays.asList(
                new Event("Search", 1)
                        .setData("url", "http://www.cdiscount.com/search/10/table+de+jardin.html")
                        .setData("referrer", "http://www.cdiscount.com"),

                new Event("whatever", 2)
                        .setData("irrelevant", "data"),

                new Event("View", 3)
                        .setData("url", "http://www.cdiscount.com/maison/jardin-plein-air/salon-de-jardin-10-personnes/f-117850208-auc3606504335784.html")
                        .setData("referrer", "http://www.cdiscount.com/search/10/table+de+jardin.html"),

                new Event("whatever", 4)
                        .setData("irrelevant", "data"),

                new Event("View", 5)
                        .setData("url", "http://www.cdiscount.com/maison/jardin-plein-air/salon-de-jardin-1-table-4-fauteuils-et-2-repose-p/f-117850208-cbdhs1701kd.html")
                        .setData("referrer", "http://www.cdiscount.com/search/10/table+de+jardin.html"),

                new Event("AddBasket", 6)
                        .setData("url", "http://www.cdiscount.com/maison/jardin-plein-air/salon-de-jardin-10-personnes/r2-117850208-auc3606504335784-972326.html")
                        .setData("referrer", "http://www.cdiscount.com/search/10/table+de+jardin.html")
                        .setData("category", "TV")
                        .setData("rating", 5))


                .stream();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection patterns() {
        return Arrays.asList(
                new Object[][]{
                        {
                                " Detect scenario : Search -> AddBasket ",
                                new FollowedBy(
                                        new Atom("Search"),
                                        new Atom("AddBasket")
                                                .addPredicate(new StringPredicateArity2(
                                                        new FieldAtom("0", "url"),
                                                        new FieldAtom("1", "referrer"),
                                                        (x, y) -> x.getValue().equals(y.getValue())))
                                                .addPredicate(new StringPredicateArity1(
                                                        new FieldAtom("1", "category"),
                                                        x -> x.getValue().equals("TV")))
                                                .addPredicate(new LongPredicateArity1(
                                                        new FieldAtom("1", "rating"),
                                                        x -> x.getValue() > 3))),
                                Arrays.asList(
                                        new Event("Search", 1),
                                        new Event("AddBasket", 6)),
                                AutomatonRunnerType.Deterministic
                        }
                });
    }
}
