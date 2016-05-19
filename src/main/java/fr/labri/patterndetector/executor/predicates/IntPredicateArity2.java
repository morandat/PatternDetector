package fr.labri.patterndetector.executor.predicates;

import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.IntegerValue;
import fr.labri.patterndetector.types.StringValue;

import java.util.ArrayList;
import java.util.function.BiPredicate;

/**
 * Created by wbraik on 5/18/2016.
 */
public class IntPredicateArity2 implements IPredicate {

    private String _field1;
    private String _field2;
    private BiPredicate<IntegerValue, IntegerValue> _predicate;

    public IntPredicateArity2(String field1, String field2, BiPredicate<IntegerValue, IntegerValue> predicate) {
        _field1 = field1;
        _field2 = field2;
        _predicate = predicate;
    }

    public ArrayList<String> getFields() {
        ArrayList<String> fields = new ArrayList<>();
        fields.add(_field1);
        fields.add(_field2);
        return fields;
    }

    @Override
    public boolean eval(IValue<?>... values) {
        if (values.length > 2) {
            throw new RuntimeException("Predicate : too many values to evaluate (arity 2, " + values.length + "values");
        }
        if (!(values[0] instanceof IntegerValue) || !(values[1] instanceof IntegerValue)) {
            throw new RuntimeException("Predicate : wrong value types for evaluation");
        }
        return _predicate.test((IntegerValue) values[0], (IntegerValue) values[1]);
    }
}
