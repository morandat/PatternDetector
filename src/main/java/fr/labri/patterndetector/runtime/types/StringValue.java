package fr.labri.patterndetector.runtime.types;

/**
 * Created by wbraik on 09/05/16.
 */
public class StringValue implements IValue<String> {
   public final String _value;

    private StringValue(String value) {
        _value = value;
    }

    public static StringValue from(String value) {
        return new StringValue(value);
    }

    public final static StringValue EMPTY = new StringValue("");

    @Override
    public String getValue() {
        return _value;
    }

    @Override
    public int getTypeID() {
        return IValue.STRING;
    }

    @Override
    public String toString() {
        return _value;
    }
}
