package fr.labri.patterndetector.compiler;

import fr.labri.patterndetector.rules.*;

/**
 * Created by wbraik on 20/11/15.
 * <p>
 * TODO Compiles a rule into a corresponding automaton by traversing the rule tree.
 */
public class RuleCompiler extends RuleVisitor {

    @Override
    public void visit(Atom rule) {
        rule.accept(this);
    }

    @Override
    public void visit(FollowedBy rule) {
        rule.accept(this);
    }

    @Override
    public void visit(Kleene rule) {
        rule.accept(this);
    }
}
