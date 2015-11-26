package fr.labri.patterndetector.rule.visitors;

import fr.labri.patterndetector.rule.IAtom;
import fr.labri.patterndetector.rule.IRule;

import java.util.ArrayList;

/**
 * Created by wbraik on 19/11/15.
 */
@Deprecated
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
