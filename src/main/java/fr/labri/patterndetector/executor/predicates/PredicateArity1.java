package fr.labri.patterndetector.executor.predicates;

import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.IntegerValue;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Created by wbraik on 5/18/2016.
 */
public class PredicateArity1<T extends IValue<?>> implements IPredicate<T> {

    private String _field;
    private Predicate<T> _predicate;

    public PredicateArity1(String field, Predicate<T> predicate) {
        _field = field;
        _predicate = predicate;
    }

    public ArrayList<String> getFields() {
        ArrayList<String> fields = new ArrayList<>();
        fields.add(_field);

        return fields;
    }

    @Override
    public boolean eval(ArrayList<T> values) {
        return _predicate.test(values.get(0));
    }
}
