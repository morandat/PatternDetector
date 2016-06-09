package fr.labri.patterndetector.executor.predicates;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldKleeneStaticIndex extends FieldAtom {

    private int _index;

    public FieldKleeneStaticIndex(String patternId, String fieldName, int index) {
        super(patternId, fieldName);
        _index = index;
    }

    public int getIndex() {
        return _index;
    }
}
