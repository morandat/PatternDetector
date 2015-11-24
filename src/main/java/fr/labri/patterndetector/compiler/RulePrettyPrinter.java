package fr.labri.patterndetector.compiler;

import fr.labri.patterndetector.rules.*;

import java.io.PrintStream;

/**
 * Created by wbraik on 20/11/15.
 */

// FIXME it's not what I want (see the r.toString() way)
public class RulePrettyPrinter {

    PrintStream _out;

    public RulePrettyPrinter(PrintStream out) {
        _out = out;
    }

    public void prettyPrint(IRule rule) {
        print(0, (rule instanceof IAtom ? rule.toString() : rule.getSymbol()));
        rule.accept(new PrettyPrintVisitor(50));
    }

    void print(int column, String string) {
        String indent = "";
        for (int i = 0; i < column; i++) {
            indent += " ";
        }
        _out.print(indent);
        _out.println(string);
    }


    class PrettyPrintVisitor extends AbstractRuleVisitor {
        int _column;

        PrettyPrintVisitor(int column) {
            _column = column;
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

            _out.print(indent + (k instanceof IAtom ? k : k.getSymbol()));
            _out.println();

            k.accept(new PrettyPrintVisitor(_column));
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

            _out.print(indentLeft + (right instanceof IAtom ? right : right.getSymbol()));
            _out.print(indentRight + (left instanceof IAtom ? left : left.getSymbol()));
            _out.println();

            left.accept(new PrettyPrintVisitor(_column - 2));
            right.accept(new PrettyPrintVisitor(_column + 2));
        }

    }
}
