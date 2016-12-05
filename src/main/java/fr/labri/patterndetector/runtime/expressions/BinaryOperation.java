package fr.labri.patterndetector.runtime.expressions;

import fr.labri.patterndetector.runtime.types.DoubleValue;
import fr.labri.patterndetector.runtime.types.IValue;
import fr.labri.patterndetector.runtime.types.LongValue;
import fr.labri.patterndetector.runtime.types.StringValue;

import static fr.labri.patterndetector.runtime.types.IValue.*;

/**
 * Created by morandat on 05/12/2016.
 */
public abstract class BinaryOperation<R> {
    private final IField _fields[];

    public BinaryOperation(IField... fields) {
        assert fields.length == 2;
        _fields = fields;
    }

    public IField[] getFields() {
        return _fields;
    }

    protected abstract R defaultValue();

    protected R evaluate(IValue<?> first, IValue<?> second) {
        switch(subtypeValue(first.getTypeID(), second.getTypeID())) {
            case STRING_STRING:
                return evaluate(((StringValue)first)._value, ((StringValue)second)._value);
            case STRING_DOUBLE:
                return evaluate(((StringValue)first)._value, ((DoubleValue)second)._value);
            case DOUBLE_STRING:
                return evaluate(((DoubleValue)first)._value, ((StringValue)second)._value);
            case DOUBLE_DOUBLE:
                return evaluate(((DoubleValue)first)._value, ((DoubleValue)second)._value);
            case STRING_LONG:
                return evaluate(((StringValue)first)._value, ((LongValue)second)._value);
            case LONG_STRING:
                return evaluate(((LongValue)first)._value, ((StringValue)second)._value);
            case DOUBLE_LONG:
                return evaluate(((DoubleValue)first)._value, ((LongValue)second)._value);
            case LONG_DOUBLE:
                return evaluate(((LongValue)first)._value, ((DoubleValue)second)._value);
            case LONG_LONG:
                return evaluate(((LongValue)first)._value, ((LongValue)second)._value);
        }
        return defaultValue();
    }

    abstract public R evaluate(String first, String second);

    public R evaluate(double first, String second) {
        return evaluate(Double.toString(first), second);
    }

    public R evaluate(long first, String second) {
        return evaluate(Long.toString(first), second);
    }

    public R evaluate(String first, double second) {
        return evaluate(first, Double.toString(second));
    }

    public R evaluate(String first, long second) {
        return evaluate(first, Long.toString(second));
    }

    public R evaluate(double first, double second) {
        return evaluate(Double.toString(first), Double.toString(second));
    }

    public R evaluate(double first, long second) {
        return evaluate(first, (double) second);
    }

    public R evaluate(long first, double second) {
        return evaluate((double) first, (double) second);
    }

    public R evaluate(long first, long second) {
        return evaluate((double) first, (double) second);
    }
}
