package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.runtime.predicates.FieldAtom;
import fr.labri.patterndetector.runtime.predicates.FieldKleeneDynamicIndex;
import fr.labri.patterndetector.runtime.predicates.IntPredicateArity2;
import fr.labri.patterndetector.rule.visitors.RuleAutomatonMaker;
import fr.labri.patterndetector.rule.visitors.RuleNamer;
import fr.labri.patterndetector.rule.visitors.RulePrinter;
import fr.labri.patterndetector.rule.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Main class for quick testing.
 * Use the JUnit test suite for true testing.
 */
public class Main {

    public static void main(String[] args) {
        IRule k = new FollowedBy("a",
                new FollowedBy(
                        new Kleene("b")
                                .addPredicate(new IntPredicateArity2(
                                        new FieldAtom("0", "x"),
                                        new FieldKleeneDynamicIndex("1", "x", i -> i),
                                        (x, y) -> x.getValue() == y.getValue())),
                        "c"));

        RuleNamer.nameRules(k);
        RulePrinter.printRule(System.out, k);

        RuleManager ruleManager = new RuleManager();
        Detector detector = new Detector(ruleManager);
        ruleManager.addRule(k, AutomatonRunnerType.Deterministic);
        detector.detect(generate());
    }

    private static Stream<? extends IEvent> generate() {
        return Arrays.asList(
                new Event("a", 1).setData("x", 10),
                new Event("b", 2).setData("y", 15),
                new Event("b", 3).setData("x", 10),
                new Event("b", 4).setData("x", 12),
                new Event("b", 5).setData("y", 10),
                new Event("c", 6).setData("y", 15),
                new Event("c", 7).setData("y", 14)
        ).stream();
    }
}
