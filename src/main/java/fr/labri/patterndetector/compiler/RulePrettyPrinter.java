package fr.labri.patterndetector.compiler;

import fr.labri.patterndetector.rules.*;

/**
 * Created by wbraik on 20/11/15.
 */
public class RulePrettyPrinter extends AbstractRuleVisitor {

    int _row;
    int _column;

    public RulePrettyPrinter() {
        _row = 0;
        _column = 50;
    }

    private RulePrettyPrinter(int row, int column) {
        _row = row;
        _column = column;
    }

    public void prettyPrint(IRule rule) {
        String indent = "";
        for (int i = 0; i < _column; i++) {
            indent += " ";
        }

        System.out.println(indent + (rule instanceof IAtom ? rule : rule.getSymbol()));

        rule.accept(this);
    }

    @Override
    public void visit(Atom atom) {

    }

    @Override
    public void visit(Kleene kleene) {
        IRule k = kleene.getChildRule();

        String indent = "";
        for (int i = 0; i < _column; i++) {
            indent += " ";
        }

        System.out.print(indent + (k instanceof IAtom ? k : k.getSymbol()));
        System.out.println();

        k.accept(new RulePrettyPrinter(_row + 1, _column));
    }

    @Override
    public void visit(FollowedBy followedBy) {
        IRule left = followedBy.getLeftChildRule();
        IRule right = followedBy.getRightChildRule();

        String indentLeft = "";
        for (int i = 0; i < _column - 2; i++) {
            indentLeft += " ";
        }
        String indentRight = "";
        for (int i = 0; i < 3; i++) {
            indentRight += " ";
        }

        System.out.print(indentLeft + (right instanceof IAtom ? right : right.getSymbol()));
        System.out.print(indentRight + (left instanceof IAtom ? left : left.getSymbol()));
        System.out.println();

        right.accept(new RulePrettyPrinter(_row + 1, _column - 2));
        left.accept(new RulePrettyPrinter(_row + 1, _column + 2));
    }
}
