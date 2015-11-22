package fr.labri.patterndetector.compiler;

import fr.labri.patterndetector.rules.IAtom;
import fr.labri.patterndetector.rules.IRule;

import java.util.ArrayList;

/**
 * Created by wbraik on 19/11/15.
 */
public class AtomCollector extends DefaultTraversal {

    private ArrayList<IAtom> atoms = new ArrayList<>();

    public ArrayList<IAtom> collect(IRule rule) {
        rule.accept(this);

        return atoms;
    }

    @Override
    public void visit(IAtom rule) {
        atoms.add(rule);
    }
}
