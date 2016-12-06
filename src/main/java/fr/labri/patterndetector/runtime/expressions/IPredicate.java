package fr.labri.patterndetector.runtime.expressions;

import java.io.Serializable;

import static fr.labri.patterndetector.runtime.expressions.IValue.DOUBLE;
import static fr.labri.patterndetector.runtime.expressions.IValue.LONG;
import static fr.labri.patterndetector.runtime.expressions.IValue.STRING;

/**
 * Created by wbraik on 5/18/2016.
 */
public interface IPredicate extends Serializable {

    IField[] getFields();

    boolean eval(IValue<?>... values);

    /**
     * Created by morandat on 02/12/2016.
     */
    abstract class Unary implements IPredicate {

        private final IField _fields[];

        public Unary(IField... fields) {
            assert fields.length == 1;
            _fields = fields;
        }

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
                    return evaluate(((IValue.StringValue) value)._value);
                case DOUBLE:
                    return evaluate(((IValue.DoubleValue) value)._value);
                case LONG:
                    return evaluate(((IValue.LongValue) value)._value);

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

    /**
     * Created by morandat on 02/12/2016.
     */
    abstract class BinaryStringPredicate extends BinaryOperation.StringOperation<Boolean> implements IPredicate {
        public BinaryStringPredicate(IField... fields) {
            super(fields);
        }

        @Override
        protected Boolean defaultValue() {
            return false;
        }

        public boolean eval(IValue<?>... values) {
            assert values.length == 2;
            return evaluate(values[0], values[1]);
        }
    }

    /**
     * Created by morandat on 02/12/2016.
     */
    abstract class BinaryNumberPredicate extends BinaryOperation.StringOperation<Boolean> implements IPredicate {
        public BinaryNumberPredicate(IField... fields) {
            super(fields);
        }

        @Override
        protected Boolean defaultValue() {
            return false;
        }

        public boolean eval(IValue<?>... values) {
            assert values.length == 2;
            return evaluate(values[0], values[1]);
        }
    }
}
