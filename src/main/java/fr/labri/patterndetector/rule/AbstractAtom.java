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

    public AbstractAtom(String symbol, String eventType) {
        super(symbol);
        _eventType = eventType;
    }

    @Override
    public String getEventType() {
        return _eventType;
    }
}
