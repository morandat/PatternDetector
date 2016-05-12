package fr.labri.patterndetector.types;

/**
 * Created by wbraik on 09/05/16.
 */
public abstract class AbstractValue<T> implements IValue<T> {

    protected T _value;

    public AbstractValue(T value) {
        _value = value;
    }

    @Override
    public T getValue() {
        return _value;
    }

    @Override
    public String toString() {
        return _value.toString();
    }
}
