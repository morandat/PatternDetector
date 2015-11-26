package fr.labri.patterndetector.rule;

/**
 * Created by william.braik on 08/07/2015.
 * <p>
 * A time constraint to be specified for a rule.
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
