package fr.labri.patterndetector.rules;

/**
 * Created by william.braik on 08/07/2015.
 */
public final class TimeConstraint implements ITimeConstraint {

    private int _value; // in seconds
    private boolean _transitive;

    TimeConstraint(int value) {
        _value = value;
        _transitive = false;
    }

    TimeConstraint(int value, boolean transitive) {
        _value = value;
        _transitive = transitive;
    }

    @Override
    public int getValue() {
        return _value;
    }

    @Override
    public String toString() {
        return "" + _value;
    }

    @Override
    public boolean isTransitive() {
        return _transitive;
    }
}
