package fr.labri.patterndetector.runtime.types;

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
}
