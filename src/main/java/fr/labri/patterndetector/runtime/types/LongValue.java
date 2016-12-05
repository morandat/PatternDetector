package fr.labri.patterndetector.runtime.types;

/**
 * Created by wbraik on 09/05/16.
 */
public class LongValue implements IValue<Long> {

    public final long _value;

    private LongValue(long value) {
        _value = value;
    }

    public static LongValue from(long value) {
        return new LongValue(value);
    }

    public final static LongValue ZERO = new LongValue(0);

    @Override
    public Long getValue() {
        return _value;
    }

    @Override
    public int getTypeID() {
        return IValue.LONG;
    }

    @Override
    public String toString() {
        return Long.toString(_value);
    }
}
