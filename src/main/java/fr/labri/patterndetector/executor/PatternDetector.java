package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.rules.*;

import java.util.ArrayList;

/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Main class for quick testing.
 * Use the JUnit test suite for more formal testing.
 */
public class PatternDetector {

    public static void main(String[] args) {

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
