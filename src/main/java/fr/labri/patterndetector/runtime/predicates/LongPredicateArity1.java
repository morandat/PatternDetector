package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.LongValue;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Created by wbraik on 5/18/2016.
 */
public class LongPredicateArity1 implements IPredicate {

    private IField _field;
    private Predicate<LongValue> _predicate;

    public LongPredicateArity1(IField field, Predicate<LongValue> predicate) {
        _field = field;
        _predicate = predicate;
    }

    @Override
    public ArrayList<IField> getFields() {
        ArrayList<IField> fields = new ArrayList<>();
        fields.add(_field);

        return fields;
    }

    @Override
    public boolean eval(IValue<?>... values) {
        if (values.length > 1) {
            throw new RuntimeException("Predicate : too many values to evaluate (arity 1, " + values.length + "values");
        }
        if (!(values[0] instanceof LongValue)) {
            throw new RuntimeException("Predicate : wrong value type for evaluation");
        }

        return _predicate.test((LongValue) values[0]);
    }
}
