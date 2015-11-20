package fr.labri.patterndetector.compiler;

import fr.labri.patterndetector.rules.Atom;
import fr.labri.patterndetector.rules.FollowedBy;
import fr.labri.patterndetector.rules.IAtom;
import fr.labri.patterndetector.rules.Kleene;

/**
 * Created by wbraik on 20/11/15.
 */
public class RulePrettyPrinter extends RuleVisitor {

    private int _indent;
    private boolean _newLine;
    private boolean _haveLeftSibling;
    private int _parentIndent;

    public RulePrettyPrinter() {
        _indent = 50;
        _newLine = true;
        _haveLeftSibling = false;
        _parentIndent = -1;
    }

    private RulePrettyPrinter(int indent, boolean newLine, boolean haveLeftSibling, int parentIndent) {
        _indent = indent;
        _newLine = newLine;
        _haveLeftSibling = haveLeftSibling;
        _parentIndent = parentIndent;
    }

    @Override
    public void visit(Atom atom) {
        String indent = "";
        for (int i = 0; i < _indent - (_haveLeftSibling ? _parentIndent : 0); i++) {
            indent += " ";
        }

        System.out.print(indent + atom);

        if (_newLine)
            System.out.println();
    }

    @Override
    public void visit(Kleene kleene) {
        String indent = "";
        for (int i = 0; i < _indent - (_haveLeftSibling ? _parentIndent : 0); i++) {
            indent += " ";
        }

        System.out.print(indent + kleene.getSymbol());

        if (_newLine)
            System.out.println();

        kleene.getChildRule().accept(new RulePrettyPrinter(_indent, true, false, _indent));
    }

    @Override
    public void visit(FollowedBy followedBy) {
        String indent = "";
        for (int i = 0; i < _indent - (_haveLeftSibling ? _parentIndent : 0); i++) {
            indent += " ";
        }

        System.out.print(indent + followedBy.getSymbol());

        if (_newLine)
            System.out.println();

        followedBy.getLeftChildRule().accept(new RulePrettyPrinter(_indent - 1, false, false, _indent));
        followedBy.getRightChildRule().accept(new RulePrettyPrinter(_indent + 3, true, true, _indent));
    }
}
