package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.executor.predicates.IPredicate;
import fr.labri.patterndetector.executor.predicates.PredicateArity1;
import fr.labri.patterndetector.rule.visitors.RuleAutomatonMaker;
import fr.labri.patterndetector.rule.visitors.RuleNamer;
import fr.labri.patterndetector.rule.visitors.RulePrinter;
import fr.labri.patterndetector.rule.*;
import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.IntegerValue;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Main class for quick testing.
 * Use the JUnit test suite for more formal testing.
 */
public class Main {

    public static void main(String[] args) {
        //IRule r = new Atom("a");
        //IRule r = new FollowedBy("a", "b");
        // .setTimeConstraint(5);
        /*IRule r = new FollowedBy(new Kleene("a").setTimeConstraint(5),
                "b");*/
        //IRule r = new FollowedBy(new Kleene("a"), "b");
        //IRule r = new Kleene(new FollowedBy("a", "b"));
        //IRule r = new FollowedBy(new Kleene(new FollowedBy("x", "y")), new FollowedBy("b", new Kleene("c")));
        /*IRule r = new Or(
                new FollowedBy(new Kleene("a"), "b"),
                new FollowedBy("a", "b"));*/

        IRule r = new FollowedBy("q", new FollowedBy("f", "a"));

        RulePrinter.printRule(System.out, r);

        IRuleAutomaton automaton = RuleAutomatonMaker.makeAutomaton(r);
        System.out.println("NFA : " + automaton);
        System.out.println("DFA : " + automaton.powerset());

        //new Atom("a").addPredicate(
        ArrayList<IntegerValue> values = new ArrayList<>();
        values.add(new IntegerValue(20));
        System.out.println(new PredicateArity1<IntegerValue>("a.x", x -> x.getValue() > 15).eval(values));


        IRule fb = new FollowedBy(new FollowedBy("a", "b"), "c");
        RuleNamer.nameRules(fb);
        RulePrinter.printRule(System.out, fb);
    }
}
