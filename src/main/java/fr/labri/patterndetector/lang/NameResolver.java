package fr.labri.patterndetector.lang;

import fr.labri.patterndetector.lang.AST.*;

import java.util.HashMap;
import java.util.Map;

public class NameResolver {

    final Rule _rule;
    Map<String, Pattern> _names = new HashMap<>();

    NameResolver(Rule rule) {
        _rule = rule;
    }

    void resolve() {
        collectName();
        new NameFixer().visit(_rule);
    }

    void collectName () {
        new PatternVisitor(){ // Add aliases
            @Override
            void visit(Pattern pattern) {
                for (String name: pattern._names) {
                    if (_names.containsKey(name))
                        throw new RuntimeException("Name conflict on: " + name);
                    _names.put(name, pattern);
                }
            }
        }.visit(_rule);

        final Map<String, Pattern> unique = new HashMap<>();
        new PatternVisitor() { // add direct name if it's unique
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
            }

            @Override
            void visit(KleenePattern kleene) {
                kleene._pattern.accept(this);
            }
        }.visit(_rule);

        for (Map.Entry<String, Pattern> entry: unique.entrySet()) {
            String name = entry.getKey();
            if (!_names.containsKey(name) && entry.getValue() != null)
                _names.put(name, entry.getValue());
        }
    }

    class NameFixer extends ExpressionVisitor {
        @Override
        void visit(FunctionCall call) {
            for (Expression arg: call._args)
                arg.accept(this);
        }

        @Override
        void visit(RangeSelector selector) {
            selector._left.accept(this);
            selector._right.accept(this);
        }

        @Override
        void visit(IndexSelector selector) {
            selector._index.accept(this);
        }

        @Override
        void visit(FieldSelector selector) {
            // FIXME
        }

        @Override
        void visit(SimpleSelector selector) {
            selector._reference = _names.get(selector._name);
        }
    }
}
