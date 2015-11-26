package fr.labri.patterndetector.rule;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by wbraik on 11/17/2015.
 * <p>
 * Base class for atoms and atom negations.
 */
public abstract class AbstractAtom extends AbstractTerminalRule implements IAtom {

    protected String _eventType; // The event type
    protected Map<String, Predicate<Integer>> _predicates; // Maps fields of the event's payload to predicates

    public AbstractAtom(String symbol, String eventType) {
        super(symbol);
        _eventType = eventType;
        _predicates = null;
    }

    public AbstractAtom(String eventType, Map<String, Predicate<Integer>> predicates) {
        super(null);
        _eventType = eventType;
        _predicates = predicates;
    }

    @Override
    public String getEventType() {
        return _eventType;
    }

    @Override
    public Map<String, Predicate<Integer>> getPredicates() {
        return _predicates;
    }

    @Override
    public IAtom addPredicate(String field, Predicate<Integer> predicate) {
        if (_predicates == null) {
            _predicates = new HashMap<>();
        }

        _predicates.put(field, predicate);

        return this;
    }
}
