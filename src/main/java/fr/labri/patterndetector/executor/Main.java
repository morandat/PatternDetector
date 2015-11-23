package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.compiler.AtomCollector;
import fr.labri.patterndetector.compiler.RuleCompiler;
import fr.labri.patterndetector.rules.*;

import java.util.ArrayList;

/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Main class for quick testing.
 * Use the JUnit test suite for more formal testing.
 */
public class Main {

    public static void main(String[] args) {
        Main main = new Main();

        //IRule a = new Atom("a");
        //IRule r = new FollowedBy("a", "b")
        // .setTimeConstraint(5);
        IRule r = new FollowedBy(new Kleene("a").setTimeConstraint(5),
                "b");
        //IRule r = new FollowedBy(new Kleene("a"), "b");
        //IRule r = new Kleene(new FollowedBy("a", "b"));
        //IRule r = new FollowedBy(new Kleene(new FollowedBy("x", "y")), new FollowedBy("b", new Kleene("c")));

        //DefaultTraversal v = new DefaultTraversal();
        //r.accept(v);

        /*AtomCollector v = new AtomCollector();
        ArrayList<IAtom> atoms = v.collect(r);
        System.out.println(atoms.size() + " atoms found :");
        atoms.forEach(System.out::println);*/

        /*ArrayList<IAtom> atoms = main.collectAtomsNotInKleene(r);
        System.out.println(atoms.size() + " atoms found :");
        atoms.forEach(System.out::println);*/

        /*RulePrettyPrinter prettyPrinter = new RulePrettyPrinter();
        prettyPrinter.prettyPrint(r);*/

        RuleCompiler compiler = new RuleCompiler();
        IRuleAutomaton automaton = compiler.compile(r);
        System.out.println(automaton);
        System.out.println(automaton.powerset());
    }
}
