package fr.labri.patterndetector.rules;

/**
 * Created by william.braik on 08/07/2015.
 */
public final class TimeConstraint implements ITimeConstraint {

    private int _value; // in seconds

    TimeConstraint(int value) {
        _value = value;
    }

    @Override
    public int getValue() {
        return _value;
    }

    @Override
    public String toString() {
        return "" + _value;
    }
}
