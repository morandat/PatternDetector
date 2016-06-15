package fr.labri.patterndetector.lang;

import fr.labri.patterndetector.lang.AST.*;

import java.io.PrintStream;

public class PrettyPrinter {

    final PrintStream _out;
    final boolean _debug;

    PrettyPrinter(PrintStream out, boolean debug) {
        _out = out;
        _debug = debug;
    }

    static final void print(PrintStream out, Rule rule) {
        print(out, rule, false);
    }

    static final void print(PrintStream out, Rule rule, boolean debug) {
        new PrettyPrinter(out, debug).print(rule);
    }

    void print(Rule rule) {
        _out.printf("%s: ", rule._name);
        new PatternPrinter().visit(rule);
        if (!rule._constraints.isEmpty()) {
            _out.printf("\n# ");
            new ConstraintPrinter().visit(rule);
        }
        _out.printf("\n\n");
    }

    class PatternPrinter extends PatternVisitor {
        @Override
        void visit(SymbolPattern symbol) {
            _out.printf("%s ", symbol);
        }

        @Override
        void visit(CompositePattern composite) {
            _out.printf("(");
            for (Pattern p: composite._patterns) {
                p.accept(this);
            }
            _out.printf(") ");
        }

        @Override
        void visit(KleenePattern kleene) {
            _out.printf("(%s)+ ", kleene._pattern);
        }
    }

    class ConstraintPrinter extends ExpressionVisitor {

        @Override
        void visit(SimpleSelector selector) {
            _out.printf("%s", selector._name);
            if (_debug && selector._field  || selector._index)
                _out.printf("{%s}", selector._field ? "f" : selector._index ? "i" : "");
        }

        @Override
        void visit(CompositeSelector selector) {
            selector._left.accept(this);
            _out.printf(".");
            selector._right.accept(this);
        }

        @Override
        void visit(IndexSelector selector) {
            _out.printf("%s[", selector._symbol);
            selector._index.accept(this);
            _out.printf("]");
        }

        @Override
        void visit(RangeSelector selector) {
            _out.printf("%s[", selector._symbol);
            _out.printf("[");
            selector._left.accept(this);
            _out.printf("..");
            selector._right.accept(this);
            _out.printf("]");
        }

        @Override
        void visit(FunctionCall call) {
            _out.printf("%s(", call._name);
            boolean first = true;
            for (Expression arg: call._args) {
                if (first)
                    first = false;
                else
                    _out.printf(", ");
                arg.accept(this);
            }
            _out.printf(") ");
        }

        @Override
        void visit(StringLiteral string) {
            _out.printf("\"%s\"", string._value.replaceAll("\"", "\\\"")); // FIXME add utility method to escape strings
        }

        @Override
        void visit(TimeValue time) {
            _out.printf("%ds", time._value.longValue()); // FIXME add utility method to convert time into the best unit
        }

        @Override
        void visit(NumberLiteral number) {
            _out.printf("%d", number._value);
        }
    }
}
