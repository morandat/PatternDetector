package fr.labri.patterndetector.executor.predicates;

import fr.labri.patterndetector.types.IntegerValue;

import java.util.ArrayList;
import java.util.function.BiPredicate;
import java.util.function.IntFunction;

/**
 * Created by wbraik on 07/06/16.
 */
public class IntPredicateArity2Kleene extends IntPredicateArity2 implements IPredicateKleene {

    private IntFunction<Integer> _indexFunction1;
    private IntFunction<Integer> _indexFunction2;

    public IntPredicateArity2Kleene(String field1, IntFunction<Integer> indexFunction1,
                                    String field2, IntFunction<Integer> indexFunction2,
                                    BiPredicate<IntegerValue, IntegerValue> predicate) {
        super(field1, field2, predicate);

        _indexFunction1 = indexFunction1;
        _indexFunction2 = indexFunction2;
    }

    @Override
    public ArrayList<IntFunction<Integer>> getIndexFunctions() {
        ArrayList<IntFunction<Integer>> indexFunctions = new ArrayList<>();
        indexFunctions.add(_indexFunction1);
        indexFunctions.add(_indexFunction2);
        return indexFunctions;
    }
}
