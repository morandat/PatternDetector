package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.*;

import java.util.Collection;

/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Main class for quick testing.
 * Use the JUnit test suite for more formal testing.
 */
public class TestRules {

    public static void main(String[] args) {
        // 0) a
        IRule r0 = new Atom("a")
                .setPredicateOnField("age", age -> age > 18)
                .setPredicateOnField("height", h -> h > 170);

        // 1) !a
        IRule r1 = new AtomNot("a");

        // 2) b --> a
        IRule r2 = new FollowedBy("b", r0)
                .setTimeConstraint(5);

        // 2.1) a --> a
        IRule r21 = new FollowedBy("a", "a");

        // 7) a+
        Kleene r7 = new Kleene("View"); // WARNING : this rule doesn't terminate (Kleene)

        // 9) a+ --> b
        IRule r9 = new FollowedBy(r7, "b");

        // 10) a --> b+
        IRule r10 = new FollowedBy("a", new Kleene("b")); // WARNING : this rule doesn't terminate because the right component is a Kleene

        // 12) a+ --> (b --> c)
        IRule r12 = new FollowedBy(new Kleene("a"), new FollowedBy("b", "c")).setTimeConstraint(5);
        /* Here the time constraint is ambiguous BECAUSE the rule (a+ -> b) is ambiguous :
        the events between the last a of a+ and the b could be part of either the Kleene OR the FollowedBy sequences.
        Because of that, the time constraint "creeps" over the Kleene sequence. */

        IRule r99 = new FollowedBy(new Kleene("View"), new Atom("Exit"));

        Collection<IEvent> events = Generator.generateStuff();

        RuleManager ruleManager = RuleManager.getInstance();
        ruleManager.addRule(r0);
        ruleManager.detect(events);
    }
}
