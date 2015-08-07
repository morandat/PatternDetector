package fr.labri.patterndetector.rules;

import java.util.concurrent.TimeUnit;

/**
 * Created by william.braik on 08/07/2015.
 */
public final class TimeConstraint implements ITimeConstraint {

    private String _eventType; // to which event types's clock this time constraint applies
    private int _value; // in seconds
    private boolean _lowerThan; // whether the time constraint specifies a min value or max value for the clock

    TimeConstraint(String eventType, int value) {
        _eventType = eventType;
        _value = value;
        _lowerThan = true;
    }

    TimeConstraint(String eventType, int value, boolean lowerThan) {
        _eventType = eventType;
        _value = value;
        _lowerThan = lowerThan;
    }

    @Override
    public int getValue() {
        return _value;
    }

    @Override
    public boolean getLowerThan() {
        return _lowerThan;
    }

    @Override
    public String getEventType() {
        return _eventType;
    }

    @Override
    public String toString() {
        return "Clk(" + _eventType + ") " + (_lowerThan ? "< " : "> ") + _value;
    }
}
