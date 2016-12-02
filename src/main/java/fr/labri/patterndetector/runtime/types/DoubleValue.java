package fr.labri.patterndetector.runtime.types;

/**
 * Created by morandat on 02/12/2016.
 */
public class DoubleValue implements IValue<Double> {
    public final double _value;

    public DoubleValue(double value) {
        _value = value;
    }

    static DoubleValue from(double value) {
        return new DoubleValue(value);
    }

    @Override
    public Double getValue() {
        return _value;
    }

    @Override
    public int getTypeID() {
        return IValue.DOUBLE;
    }
}
