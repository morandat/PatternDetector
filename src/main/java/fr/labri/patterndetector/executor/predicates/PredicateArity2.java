package fr.labri.patterndetector.executor.predicates;

import fr.labri.patterndetector.types.IValue;

import java.util.ArrayList;
import java.util.function.BiPredicate;

/**
 * Created by wbraik on 5/18/2016.
 */
public class PredicateArity2<T extends IValue<?>> implements IPredicate<T> {

    private String _field1;
    private String _field2;
    private BiPredicate<T, T> _predicate;

    public PredicateArity2(String field1, String field2, BiPredicate<T, T> predicate) {
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
    public boolean eval(ArrayList<T> values) {
        return _predicate.test(values.get(0), values.get(1));
    }
}
