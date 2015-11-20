package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.compiler.AtomCollector;
import fr.labri.patterndetector.compiler.DefaultTraversal;
import fr.labri.patterndetector.compiler.RulePrettyPrinter;
import fr.labri.patterndetector.rules.*;

import java.util.ArrayList;

/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Main class for quick testing.
 * Use the JUnit test suite for more formal testing.
 */
public class TestVisitors {

    public static void main(String[] args) {
        TestVisitors main = new TestVisitors();

        //IRule r = new Atom("a");
        IRule r = new FollowedBy(new Kleene("a"), new FollowedBy("b", "c"));
        //IRule r = new FollowedBy("a", "b");
        //IRule r = new Kleene("a");
        //IRule r = new FollowedBy(new Kleene("a"), "b");

        //DefaultTraversal v = new DefaultTraversal();
        //r.accept(v);

        /*AtomCollector v = new AtomCollector();
        ArrayList<IAtom> atoms = v.collect(r);
        System.out.println(atoms.size() + " atoms found :");
        atoms.forEach(System.out::println);*/

        /*ArrayList<IAtom> atoms = main.collectAtomsNotInKleene(r);
        System.out.println(atoms.size() + " atoms found :");
        atoms.forEach(System.out::println);*/

        RulePrettyPrinter prettyPrinter = new RulePrettyPrinter();
        r.accept(prettyPrinter);
    }

    public ArrayList<IAtom> collectAtomsNotInKleene(IRule root) {
        return new AtomCollector() {
            @Override
            public void visit(Kleene rule) {
            }
        }.collect(root);
    }
}
