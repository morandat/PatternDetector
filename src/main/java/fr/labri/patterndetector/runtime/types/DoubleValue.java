package fr.labri.patterndetector.runtime.types;

/**
 * Created by morandat on 02/12/2016.
 */
public class DoubleValue implements IValue<Double> {
    public final double _value;

    private DoubleValue(double value) {
        _value = value;
    }

    public static DoubleValue from(double value) {
        return new DoubleValue(value);
    }

    public static final DoubleValue ZERO = new DoubleValue(0);

    @Override
    public Double getValue() {
        return _value;
    }

    @Override
    public int getTypeID() {
        return IValue.DOUBLE;
    }

    @Override
    public String toString() {
        return Double.toString(_value);
    }
}
