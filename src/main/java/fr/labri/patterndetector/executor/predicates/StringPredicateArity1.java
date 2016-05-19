package fr.labri.patterndetector.executor.predicates;

import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.IntegerValue;
import fr.labri.patterndetector.types.StringValue;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Created by wbraik on 5/18/2016.
 */
public class StringPredicateArity1 implements IPredicate {

    private String _field;
    private Predicate<StringValue> _predicate;

    public StringPredicateArity1(String field, Predicate<StringValue> predicate) {
        _field = field;
        _predicate = predicate;
    }

    public ArrayList<String> getFields() {
        ArrayList<String> fields = new ArrayList<>();
        fields.add(_field);

        return fields;
    }

    @Override
    public boolean eval(IValue<?>... values) {
        if (values.length > 1) {
            throw new RuntimeException("Predicate : too many values to evaluate (arity 1, " + values.length + "values");
        }
        if (!(values[0] instanceof StringValue)) {
            throw new RuntimeException("Predicate : wrong value type for evaluation");
        }
        return _predicate.test((StringValue) values[0]);
    }
}
