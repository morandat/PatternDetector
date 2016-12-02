package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.types.IValue;
import fr.labri.patterndetector.runtime.types.*;

import static fr.labri.patterndetector.runtime.types.IValue.*;

/**
 * Created by morandat on 02/12/2016.
 */
public abstract class Predicate2 implements IPredicate {

    private final IField _fields[];

    public Predicate2(IField firstField, IField secondField) {
        _fields = new IField[]{ firstField, secondField };
    }

    @Override
    public IField[] getFields() {
        return _fields;
    }

    @Override
    public boolean eval(IValue<?>... values) {
        assert values.length == 2;
        return evaluate(values[0], values[1]);
    }

    boolean evaluate(IValue<?> first, IValue<?> second) {
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
        return false;
    }

    abstract public boolean evaluate(String first, String second);

    public boolean evaluate(double first, String second) {
        return evaluate(Double.toString(first), second);
    }

    public boolean evaluate(long first, String second) {
        return evaluate(Long.toString(first), second);
    }

    public boolean evaluate(String first, double second) {
        return evaluate(first, Double.toString(second));
    }

    public boolean evaluate(String first, long second) {
        return evaluate(first, Long.toString(second));
    }

    public boolean evaluate(double first, double second) {
        return evaluate(Double.toString(first), Double.toString(second));
    }

    public boolean evaluate(double first, long second) {
        return evaluate(first, (double) second);
    }

    public boolean evaluate(long first, double second) {
        return evaluate((double) first, (double) second);
    }

    public boolean evaluate(long first, long second) {
        return evaluate((double) first, (double) second);
    }
}
