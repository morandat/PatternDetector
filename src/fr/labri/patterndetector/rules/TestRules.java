package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.*;

import java.util.Collection;

/**
 * Created by William Braik on 6/22/2015.
 */
public class TestRules {

    public static void main(String[] args) {
        // 0) a
        IRule r0 = new Atom("a");

        // 1) !a
        IRule r1 = new AtomNot("a");

        // 2) a --> b
        IRule r2 = new FollowedBy(new Atom("b"), r0);

        // 4) a && b
        //IRule r4 = new And(new Atom("a"), new Atom("b")); // TODO operator not implemented yet

        // 5) a || b
        //IRule r5 = new Or(new Atom("a"), new Atom("b")); // TODO operator not implemented yet

        // 6) a+.
        IRule r6 = new KleeneContiguous(new Atom("a"));

        // 7) a+
        IRule r7 = new Kleene(new Atom("a")); // WARNING : this rule doesn't terminate (Kleene)

        // 8) a . b
        IRule r8 = new FollowedByContiguous(new Atom("a"), new Atom("b"));

        // 9) a+ --> b
        IRule r9 = new FollowedBy(r7, new Atom("b"));

        // 10) a --> b+
        IRule r10 = new FollowedBy(new Atom("a"), new Kleene(new Atom("b"))); // WARNING : this rule doesn't terminate because the right component is a Kleene

        // 11) (a+ --> b) --> x+.
        IRule r11 = new FollowedBy(r9, new KleeneContiguous(new Atom("x")));

        Collection<Event> events = Generator.generateKleene();

        Runnable task = () -> {

        };
        new Thread(task).start();

        RuleManager ruleManager = new RuleManager();
        ruleManager.addRule(r7);
        ruleManager.detect(events);
    }
}
