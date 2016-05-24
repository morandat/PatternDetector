package fr.labri.patterndetector.rule;

/**
 * Created by wbraik on 5/24/2016.
 */
public abstract class AbstractKleene extends AbstractTerminalRule implements IKleene {

    protected String _eventType; // The event type

    public AbstractKleene(String symbol, String eventType) {
        super(symbol);
        _eventType = eventType;
    }

    @Override
    public String getEventType() {
        return _eventType;
    }
}
