package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.rules.*;

import java.util.ArrayList;

/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Main class for quick testing.
 * Use the JUnit test suite for more formal testing.
 */
public class TestRules {

    public static void main(String[] args) {
        IRule rule = new Atom("a");
        //.setPredicate("age", age -> age > 18)
        //.setPredicate("height", h -> h > 170);

        /*IRule rule = new AtomNot("a");

        IRule rule = new FollowedBy("a", "b");

        IRule rule = new FollowedBy("a", "a");

        Kleene rule = new Kleene("View");

        IRule rule = new FollowedBy(new Kleene("View"), "Exit");

        IRule rule = new FollowedBy("Enter", new Kleene("View"));

        IRule rule = new FollowedBy(new Kleene("View"), new FollowedBy("Add", "Exit"));*/

        RuleManager ruleManager = new RuleManager();
        ruleManager.addRule(rule);
        Detector detector = new Detector(ruleManager);
        detector.detect(Generator.generateStuff());
    }

    class DefaultTraversal extends RuleVisitor {
        @Override
        public void visit(ITerminalRule terminalRule) {
            terminalRule.accept(this);
        }

        @Override
        public void visit(ICompositeRule compositeRule) {
            compositeRule.getChildRules().forEach(rule -> rule.accept(this));
        }
    }

    class AtomCollector extends DefaultTraversal {
        ArrayList<IAtom> atoms = new ArrayList<>();

        ArrayList<IAtom> collect(IRule root) {
            root.accept(this);
            return atoms;
        }

        @Override
        public void visit(IAtom rule) {
            atoms.add(rule);
        }
    }

    public void print(IRule root) {
        new DefaultTraversal() {
            @Override
            public void visit(IRule rule) {
                System.out.println("*** VISITOR ::: " + rule.getName() + " ***");
            }
        }.startVisit(root);
    }

    public ArrayList<IAtom> collectAtomsNotInKleene(IRule root) {
        return new AtomCollector() {
            @Override
            public void visit(Kleene rule) {
            }
        }.collect(root);
    }
}
