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
        // 0) a
        IRule r0 = new Atom("a")
                .setPredicate("age", age -> age > 18)
                .setPredicate("height", h -> h > 170);

        // 1) !a
        IRule r1 = new AtomNot("a");

        // 2) b --> a
        IRule r2 = new FollowedBy("a", "b");
        //.setTimeConstraint(5);

        // 2.1) a --> a
        IRule r21 = new FollowedBy("a", "a");

        // 7) a+
        Kleene r7 = new Kleene("View"); // WARNING : this rule doesn't terminate (Kleene)

        // 9) a+ --> b
        IRule r9 = new FollowedBy(r7, "b");

        // 10) a --> b+
        IRule r10 = new FollowedBy("a", new Kleene("b")); // WARNING : this rule doesn't terminate because the right component is a Kleene

        // 12) a+ --> (b --> c)
        IRule r12 = new FollowedBy(new Kleene("a"), new FollowedBy("b", "c"));
        //.setTimeConstraint(5);

        IRule r99 = new FollowedBy(new Kleene("View"), new Atom("Exit"));

        /*Collection<IEvent> events = Generator.generateStuff();

        RuleManager ruleManager = RuleManager.getInstance();
        ruleManager.addRule(r2);
        ruleManager.detect(events);*/
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
                System.out.println(rule.getName());
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
