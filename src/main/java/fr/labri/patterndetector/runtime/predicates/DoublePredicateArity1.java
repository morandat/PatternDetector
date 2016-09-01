package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.types.DoubleValue;
import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.LongValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Created by wbraik on 5/18/2016.
 */
public class DoublePredicateArity1 implements IPredicate, Serializable {

    private IField _field;
    private Predicate<DoubleValue> _predicate;

    public DoublePredicateArity1(IField field, Predicate<DoubleValue> predicate) {
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
        if (!(values[0] instanceof DoubleValue)) {
            throw new RuntimeException("Predicate : wrong value type for evaluation");
        }

        return _predicate.test((DoubleValue) values[0]);
    }
}
