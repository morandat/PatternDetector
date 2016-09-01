package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.StringValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.BiPredicate;

/**
 * Created by wbraik on 5/18/2016.
 */
public class StringPredicateArity2 implements IPredicate, Serializable {

    private IField _field1;
    private IField _field2;
    private BiPredicate<StringValue, StringValue> _predicate;

    public StringPredicateArity2(IField field1, IField field2, BiPredicate<StringValue, StringValue> predicate) {
        _field1 = field1;
        _field2 = field2;
        _predicate = predicate;
    }

    @Override
    public ArrayList<IField> getFields() {
        ArrayList<IField> fields = new ArrayList<>();
        fields.add(_field1);
        fields.add(_field2);

        return fields;
    }

    @Override
    public boolean eval(IValue<?>... values) {
        if (values.length > 2) {
            throw new RuntimeException("Predicate : too many values to evaluate (arity 2, " + values.length + "values");
        }
        if (!(values[0] instanceof StringValue) || !(values[1] instanceof StringValue)) {
            throw new RuntimeException("Predicate : wrong value types for evaluation");
        }

        return _predicate.test((StringValue) values[0], (StringValue) values[1]);
    }
}
