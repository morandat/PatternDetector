package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.executor.predicates.IPredicate;
import fr.labri.patterndetector.rule.visitors.RuleStringifier;
import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.IntegerValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by William Braik on 6/25/2015.
 * <p>
 * Composes sub-rules and/or atoms to form patterns.
 */
public abstract class AbstractRule implements IRule {

    // Concrete subclasses should log with their own class names
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected String _name;
    protected String _symbol;
    protected ArrayList<IPredicate<IntegerValue>> _predicates;

    public AbstractRule(String symbol) {
        _symbol = symbol;
        _predicates = null;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }

    @Override
    public String getSymbol() {
        return _symbol;
    }

    @Override
    public ArrayList<IPredicate<IntegerValue>> getPredicates() {
        return _predicates;
    }

    @Override
    public IRule addPredicate(IPredicate<IntegerValue> predicate) {
        if (_predicates == null)
            _predicates = new ArrayList<>();

        _predicates.add(predicate);

        return this;
    }

    @Override
    public String toString() {
        return RuleStringifier.stringify(this);
    }
}
