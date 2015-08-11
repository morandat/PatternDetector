package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.*;

import java.util.Collection;

/**
 * Created by William Braik on 6/22/2015.
 */
public class TestRules {

    public static void main(String[] args) {
        // 0) a
        IRule r0 = new Atom("a")
                .setTimeConstraint(new TimeConstraint(10));

        // 1) !a
        IRule r1 = new AtomNot("a")
                .setTimeConstraint(new TimeConstraint(10));

        // 2) a --> b
        IRule r2 = new FollowedBy("b", r0)
                .setTimeConstraint(new TimeConstraint(5));

        // 4) a && b
        //IRule r4 = new And(new Atom("a"), new Atom("b")); // TODO operator not implemented yet

        // 5) a || b
        //IRule r5 = new Or(new Atom("a"), new Atom("b")); // TODO operator not implemented yet (syntactic sugar ?)

        // 6) a+.
        IRule r6 = new KleeneContiguous("a");

        // 7) a+
        IRule r7 = new Kleene("a"); // WARNING : this rule doesn't terminate (Kleene)

        // 8) a . b
        IRule r8 = new FollowedByContiguous("a", "b");

        // 9) a+ --> b
        IRule r9 = new FollowedBy(r7, "b");

        // 10) a --> b+
        IRule r10 = new FollowedBy("a", new Kleene("b")); // WARNING : this rule doesn't terminate because the right component is a Kleene

        // 11) (a+ --> b) --> x+.
        IRule r11 = new FollowedBy(r9, new KleeneContiguous("x"));

        // 12) a+ --> (b --> c)
        IRule r12 = new FollowedBy(new Kleene("a"), new FollowedBy("b", "c"));

        Collection<Event> events = Generator.generateStuff();

        RuleManager ruleManager = new RuleManager();
        ruleManager.addRule(r2);
        ruleManager.detect(events);
    }
}
