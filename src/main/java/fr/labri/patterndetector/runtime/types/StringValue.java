package fr.labri.patterndetector.runtime.types;

/**
 * Created by wbraik on 09/05/16.
 */
public class StringValue implements IValue<String> {
   public final String _value;

    public StringValue(String value) {
        _value = value;
    }

    static StringValue from(String value) {
        return new StringValue(value);
    }

    @Override
    public String getValue() {
        return _value;
    }

    @Override
    public int getTypeID() {
        return IValue.STRING;
    }
}
