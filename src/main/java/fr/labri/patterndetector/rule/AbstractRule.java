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
    protected ArrayList<IPredicate> _predicates;
    protected Runnable _action;

    public AbstractRule(String symbol) {
        _symbol = symbol;
        _predicates = null;
        _action = null;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public IRule setName(String name) {
        _name = name;

        return this;
    }

    @Override
    public IRule setAction(Runnable action) {
        _action = action;

        return this;
    }

    @Override
    public String getSymbol() {
        return _symbol;
    }

    @Override
    public ArrayList<IPredicate> getPredicates() {
        return _predicates;
    }

    @Override
    public Runnable getAction() {
        return _action;
    }

    @Override
    public IRule addPredicate(IPredicate predicate) {
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
