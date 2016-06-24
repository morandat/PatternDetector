package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.runtime.predicates.FieldAtom;
import fr.labri.patterndetector.rule.*;
import fr.labri.patterndetector.runtime.predicates.FieldAtomTime;
import fr.labri.patterndetector.runtime.predicates.LongPredicateArity2;
import fr.labri.patterndetector.runtime.predicates.StringPredicateArity2;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by William Braik on 6/22/2015.
 */
public class Main {

    public static void main(String[] args) {
        IRule r = new FollowedBy(
                new Atom("Search"),
                new Atom("Purchase")
                        .addPredicate(new LongPredicateArity2(
                                new FieldAtomTime("0"),
                                new FieldAtomTime("1"),
                                (x, y) -> (y.getValue() - x.getValue()) <= 5)));

        RuleManager ruleManager = new RuleManager();
        Detector detector = new Detector(ruleManager);
        ruleManager.addRule(r, AutomatonRunnerType.Deterministic);
        detector.detect(Main.generate());
    }

    private static Stream<? extends IEvent> generate() {
        return Arrays.asList(
                new Event("Search", 1)
                        .setData("url", "s")
                        .setData("referrer", "x"),
                new Event("Purchase", 6)
                        .setData("url", "p")
                        .setData("referrer", "s")
        ).stream();
    }
}
