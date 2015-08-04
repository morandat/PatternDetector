package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.*;

import java.util.Collection;

/**
 * Created by William Braik on 6/22/2015.
 */
public class TestRules {

    public static void main(String[] args) {
        // 0) a
        System.out.println("\n*** INSTANTIATING R0 ***");
        IRule r0 = new Atom("a");

        // 1) !a
        System.out.println("\n*** INSTANTIATING R1 ***");
        IRule r1 = new AtomNot("a");

        // 2) a --> b
        System.out.println("\n*** INSTANTIATING R2 ***");
        IRule r2 = new FollowedBy(new Atom("b"), r0);

        // 3) a -/-> b
        //IRule r3 = new NotFollowedBy(new Atom("a"), new Atom("b")); // TODO operator not implemented

        // 4) a && b
        //IRule r4 = new And(new Atom("a"), new Atom("b")); // TODO operator not implemented

        // 5) a || b
        //IRule r5 = new Or(new Atom("a"), new Atom("b")); // TODO operator not implemented

        // 6) a+.
        System.out.println("\n*** INSTANTIATING R6 ***");
        IRule r6 = new KleeneContiguous(new Atom("a"));

        // 7) a+
        System.out.println("\n*** INSTANTIATING R7 ***");
        IRule r7 = new Kleene(new Atom("a")); // WARNING : this rule doesn't terminate (Kleene)

        // 8) a . b
        System.out.println("\n*** INSTANTIATING R8 ***");
        IRule r8 = new FollowedByContiguous(new Atom("a"), new Atom("b"));

        // 9) a+ --> b
        System.out.println("\n*** INSTANTIATING R9 ***");
        IRule r9 = new FollowedBy(r7, new Atom("b"));

        // 10) a+. --> b
        System.out.println("\n*** INSTANTIATING R10 ***");
        IRule r10 = new FollowedBy(new Kleene(new Atom("a")), new Atom("b"));

        // 11) a --> b+
        System.out.println("\n*** INSTANTIATING R11 ***");
        // WARNING : this rule doesn't terminate because the right component is a Kleene
        IRule r11 = new FollowedBy(new Atom("a"), new Kleene(new Atom("b")));

        // 11) a --> b+
        System.out.println("\n*** INSTANTIATING R99 ***");
        IRule r99 = new FollowedBy(r9, new KleeneContiguous(new Atom("x")));

        //Collection<Event> events = Generator.generateFollowedByContiguous();
        Collection<Event> events = Generator.generateKleene();

        RuleManager ruleManager = new RuleManager(r99);

        ruleManager.detect(events);
    }
}
