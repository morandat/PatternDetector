package fr.labri.patterndetector.executor.predicates;

import java.util.function.IntFunction;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldKleeneDynamicIndex extends FieldAtom {

    private IntFunction<Integer> _indexFunc;

    public FieldKleeneDynamicIndex(String patternId, String fieldName, IntFunction<Integer> indexFunc) {
        super(patternId, fieldName);
        _indexFunc = indexFunc;
    }

    public IntFunction<Integer> getIndexFunc() {
        return _indexFunc;
    }
}
