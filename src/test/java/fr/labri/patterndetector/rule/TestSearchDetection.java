package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.executor.AutomatonRunnerType;
import fr.labri.patterndetector.executor.Event;
import fr.labri.patterndetector.executor.IEvent;
import fr.labri.patterndetector.executor.predicates.StringPredicateArity2;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

@RunWith(Parameterized.class)
public class TestSearchDetection extends AbstractTestDetection {

    public Stream<? extends IEvent> generate() {
        return searchScenario();
    }

    public static Stream<? extends IEvent> searchScenario() {
        return Arrays.asList(
                new Event("Search", 1)
                        .setData("url", "http://www.cdiscount.com/search/10/table+de+jardin.html")
                        .setData("referrer", "http://www.cdiscount.com"),
                new Event("whatever", 2)
                        .setData("irrelevant", "data"),
                new Event("ViewProduct", 3)
                        .setData("url", "http://www.cdiscount.com/maison/jardin-plein-air/salon-de-jardin-10-personnes/f-117850208-auc3606504335784.html")
                        .setData("referrer", "http://www.cdiscount.com/search/10/table+de+jardin.html"),
                new Event("whatever", 4)
                        .setData("irrelevant", "data"),
                new Event("AddBasket", 5)
                        .setData("url", "http://www.cdiscount.com/maison/jardin-plein-air/salon-de-jardin-10-personnes/r2-117850208-auc3606504335784-972326.html")
                        .setData("referrer", "http://www.cdiscount.com/maison/jardin-plein-air/salon-de-jardin-10-personnes/f-117850208-auc3606504335784.html"))
                .stream();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection patterns() {
        return Arrays.asList(
                new Object[][]{
                        {
                                " Detect search scenario : query -> viewProduct -> addProduct ",
                                new FollowedBy(new Atom("Search").setAction(() -> System.out.println("Found Search event")),
                                        new FollowedBy(
                                                new Atom("ViewProduct")
                                                        .addPredicate(new StringPredicateArity2("$0.url", "$1.referrer",
                                                                (x, y) -> x.getValue().equals(y.getValue())))
                                                        .setAction(() -> System.out.println("Then found View event")),
                                                new Atom("AddBasket")
                                                        .addPredicate(new StringPredicateArity2("$1.url", "$2.referrer",
                                                                (x, y) -> x.getValue().equals(y.getValue())))
                                                        .setAction(() -> System.out.println("And finally found Basket event")))),
                                Arrays.asList(
                                        new Event("Search", 1),
                                        new Event("ViewProduct", 3),
                                        new Event("AddBasket", 5)),
                                AutomatonRunnerType.DFA
                        }
                });
    }
}
