package fr.labri.patterndetector.lang;

import fr.labri.patterndetector.lang.AST.*;

import java.util.HashMap;
import java.util.Map;

public class NameResolver {

    final Rule _rule;
    Map<Pattern, Map<String, Pattern>> _names = new HashMap<>();

    NameResolver(Rule rule) {
        _rule = rule;
    }

    void resolve() {
        collectName(null);
        for (Expression expr: _rule._constraints)
            expr.accept(new NameFixer(null, expr));
    }

    Map<String, Pattern> getNames(Pattern p) {
        Map<String, Pattern> names = _names.get(p);
        if (names == null) {
            names = new HashMap<>();
            _names.put(p, names);
        }
        return names;
    }

    void collectName (Pattern context) { // FIXME should be recusive
        // TODO  move _names by level (i.e. pattern ?)
        new PatternVisitor(){ // Add aliases
            @Override
            void visit(Pattern pattern) {
                for (String name: pattern._names) {
                    Map<String, Pattern> names = getNames(context);
                    if (names.containsKey(name))
                        throw new RuntimeException("Name conflict on: " + name);
                    names.put(name, pattern);
                }
            }
        }.visit(_rule);

        new PatternVisitor() { // add direct name if it's unique
            final Map<String, Pattern> unique = new HashMap<>();
            void addName(String name, Pattern pattern) {
                if (unique.containsKey(name))
                    unique.put(name, null); // discard name
                else
                    unique.put(name, pattern);
            }

            @Override
            void visit(SymbolPattern symbol) {
                addName(symbol._symbol, symbol);
            }

            @Override
            void visit(CompositePattern composite) {
                if (composite._patterns.size() == 1) {
                    composite._patterns.get(0).accept(this);
                }
                for (Pattern child: composite._patterns) {
                    collectName(composite); // FIXME bad way to recurse, but it works
                }
            }

            @Override
            void visit(KleenePattern kleene) {
                kleene._pattern.accept(this);
            }

            @Override
            void visit(Rule r) {
                super.visit(r);
                for (Map.Entry<String, Pattern> entry: unique.entrySet()) {
                    String name = entry.getKey();
                    Map<String, Pattern> names = getNames(context);
                    if (!names.containsKey(name) && entry.getValue() != null)
                        names.put(name, entry.getValue());
                }
            }
        }.visit(_rule);
    }

    class NameFixer extends ExpressionVisitor {
        final Pattern _context;
        final Expression _expr;

        NameFixer(Pattern context, Expression expr) {
            _context = context;
            _expr = expr;
        }

        @Override
        void visit(FunctionCall call) {
            for (Expression arg: call._args)
                arg.accept(this);
        }

        @Override
        void visit(RangeSelector selector) {
            selector._reference = findPattern(selector._symbol);
            selector._left.accept(new NameFixer(selector._reference, _expr));
            selector._right.accept(new NameFixer(selector._reference, _expr));
        }

        @Override
        void visit(IndexSelector selector) {
            selector._index.accept(this);
        }

        @Override
        void visit(CompositeSelector selector) {
            selector._left.accept(this);
            selector._right.accept(new NameFixer(selector._right._reference, _expr));
        }

        @Override
        void visit(SimpleSelector selector) {
            selector._reference = findPattern(selector._name);
        }

        Pattern findPattern(String name) {
            Pattern reference = getNames(_context).get(name);
            if (reference == null)
                throw new RuntimeException(String.format("Unknown reference to %s in %s", name, _expr));
            return reference;
        }
    }
}
