package fr.labri.patterndetector.runtime.types;

/**
 * Created by wbraik on 09/05/16.
 */
public class LongValue implements IValue<Long> {

    public final long _value;

    public LongValue(long value) {
        _value = value;
    }

    static LongValue from(long value) {
        return new LongValue(value);
    }

    @Override
    public Long getValue() {
        return _value;
    }

    @Override
    public int getTypeID() {
        return IValue.LONG;
    }
}
