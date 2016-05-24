package fr.labri.patterndetector.rule.visitors;

import fr.labri.patterndetector.rule.*;

/**
 * Created by wbraik on 19/11/15.
 */
public interface IRuleVisitor {

    void visit(IRule rule);

    void visit(AbstractTerminalRule terminalRule);

    void visit(AbstractCompositeRule compositeRule);

    void visit(AbstractUnaryRule unaryRule);

    void visit(AbstractBinaryRule binaryRule);

    void visit(AbstractAtom atom);

    void visit(Atom atom);

    void visit(Kleene kleene);

    void visit(FollowedBy followedBy);

    void visit(Or or);
}
