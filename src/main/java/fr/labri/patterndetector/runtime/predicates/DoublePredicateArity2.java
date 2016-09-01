package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.types.DoubleValue;
import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.LongValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.BiPredicate;

/**
 * Created by wbraik on 5/18/2016.
 */
public class DoublePredicateArity2 implements IPredicate, Serializable {

    private IField _field1;
    private IField _field2;
    private BiPredicate<DoubleValue, DoubleValue> _predicate;

    public DoublePredicateArity2(IField field1, IField field2, BiPredicate<DoubleValue, DoubleValue> predicate) {
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
        if (!(values[0] instanceof DoubleValue) || !(values[1] instanceof DoubleValue)) {
            throw new RuntimeException("Predicate : wrong value types for evaluation");
        }

        return _predicate.test((DoubleValue) values[0], (DoubleValue) values[1]);
    }
}
