package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.types.IValue;
import fr.labri.patterndetector.runtime.types.*;

import static fr.labri.patterndetector.runtime.types.IValue.*;

/**
 * Created by morandat on 02/12/2016.
 */
public abstract class Predicate1 implements IPredicate {

    private final IField _fields[];

    public Predicate1(IField field) {
        _fields = new IField[]{field};
    }

    @Override
    public IField[] getFields() {
        return _fields;
    }

    @Override
    public boolean eval(IValue<?>... values) {
        assert values.length == 1;
        return evaluate(values[0]);
    }

    boolean evaluate(IValue<?> value) {
        switch (value.getTypeID()) {
            case STRING:
                return evaluate(((StringValue) value)._value);
            case DOUBLE:
                return evaluate(((DoubleValue) value)._value);
            case LONG:
                return evaluate(((LongValue) value)._value);

        }
        return false;
    }

    abstract public boolean evaluate(String value);

    public boolean evaluate(double value) {
        return evaluate(Double.toString(value));
    }

    public boolean evaluate(long value) {
        return evaluate((double) value);
    }
}
