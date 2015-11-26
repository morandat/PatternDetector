package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.rule.visitors.RuleAutomatonBuilder;
import fr.labri.patterndetector.rule.visitors.RulePrinter;
import fr.labri.patterndetector.rule.*;

/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Main class for quick testing.
 * Use the JUnit test suite for more formal testing.
 */
public class Main {

    public static void main(String[] args) {

        Main main = new Main();
        RulePrinter stdoutRulePrinter = new RulePrinter(System.out);

        //IRule r = new Atom("a");
        //IRule r = new FollowedBy("a", "b");
        // .setTimeConstraint(5);
        /*IRule r = new FollowedBy(new Kleene("a").setTimeConstraint(5),
                "b");*/
        //IRule r = new FollowedBy(new Kleene("a"), "b");
        //IRule r = new Kleene(new FollowedBy("a", "b"));
        IRule r = new FollowedBy(new Kleene(new FollowedBy("x", "y")), new FollowedBy("b", new Kleene("c")));

        stdoutRulePrinter.printRule(r);

        RuleAutomatonBuilder compiler = new RuleAutomatonBuilder();
        IRuleAutomaton automaton = compiler.buildAutomaton(r);
        System.out.println(automaton);
        System.out.println(automaton.powerset());
    }
}
