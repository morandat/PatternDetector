package fr.labri.patterndetector.compiler;

import fr.labri.patterndetector.rules.*;

/**
 * Created by wbraik on 19/11/15.
 */
public interface IRuleVisitor {

    void visit(IRule rule);

    void visit(ITerminalRule terminalRule);

    void visit(ICompositeRule compositeRule);

    void visit(IUnaryRule unaryRule);

    void visit(IBinaryRule binaryRule);

    void visit(IAtom atom);

    void visit(Atom atom);

    void visit(Kleene kleene);

    void visit(FollowedBy followedBy);
}
