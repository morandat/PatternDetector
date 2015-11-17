package fr.labri.patterndetector.rules;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by wbraik on 11/17/2015.
 * <p>
 * Base class for atoms and atom negations.
 */
public abstract class AbstractAtom extends AbstractRule implements IAtom {

    protected String _eventType; // The event type
    protected Map<String, Predicate<Integer>> _predicates; // Maps fields of the event's payload to predicates TODO à mettre au niveau règle ?

    public AbstractAtom(String eventType) {
        super(null);
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
    public IAtom addPredicate(String field, Predicate<Integer> predicate) {
        if (_predicates == null) {
            _predicates = new HashMap<>();
        }

        _predicates.put(field, predicate);

        return this;
    }
}
