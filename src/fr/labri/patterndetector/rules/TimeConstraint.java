package fr.labri.patterndetector.rules;

import java.util.concurrent.TimeUnit;

/**
 * Created by william.braik on 08/07/2015.
 */
public class TimeConstraint implements ITimeConstraint {

    private int _value;
    private TimeUnit _unit;

    TimeConstraint(int value, TimeUnit unit) {
        _value = value;
        _unit = TimeUnit.SECONDS;
    }

    @Override
    public int getValue() {
        return _value;
    }

    @Override
    public TimeUnit getUnit() {
        return _unit;
    }

    @Override
    public String toString() {
        return _value + _unit.toString();
    }
}
