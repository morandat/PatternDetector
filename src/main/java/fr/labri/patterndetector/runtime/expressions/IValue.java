package fr.labri.patterndetector.runtime.expressions;

/**
 * Created by wbraik on 09/05/16.
 */
public interface IValue<T> {

    T getValue();

    int getTypeID();

    int STRING = 0;
    int DOUBLE = 1;
    int LONG = 2;

    int BIT_COUNT = 2;

    static int subtypeValue(int first, int second) {
        return first << BIT_COUNT | second;
    }

    int STRING_STRING = STRING << BIT_COUNT | STRING;
    int STRING_DOUBLE = STRING << BIT_COUNT | DOUBLE;
    int DOUBLE_STRING = DOUBLE << BIT_COUNT | STRING;
    int DOUBLE_DOUBLE = DOUBLE << BIT_COUNT | DOUBLE;
    int STRING_LONG = STRING << BIT_COUNT | LONG;
    int LONG_STRING = LONG << BIT_COUNT | STRING;
    int DOUBLE_LONG = DOUBLE << BIT_COUNT | LONG;
    int LONG_DOUBLE = LONG << BIT_COUNT | DOUBLE;
    int LONG_LONG = LONG << BIT_COUNT | LONG;

    /**
     * Created by morandat on 02/12/2016.
     */
    class DoubleValue implements IValue<Double> {
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

    /**
     * Created by wbraik on 09/05/16.
     */
    class LongValue implements IValue<Long> {

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

    /**
     * Created by wbraik on 09/05/16.
     */
    class StringValue implements IValue<String> {
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
}
