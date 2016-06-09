package fr.labri.patterndetector.executor.predicates;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldKleeneRange extends FieldAtom {

    private int _startIndex;
    private int _endIndex;

    public FieldKleeneRange(String patternId, String fieldName, int startIndex, int endIndex) {
        super(patternId, fieldName);
        _startIndex = startIndex;
        _endIndex = endIndex;
    }

    public int getStartIndex() {
        return _startIndex;
    }

    public int getEndIndex() {
        return _endIndex;
    }
}
