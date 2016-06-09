package fr.labri.patterndetector.executor.predicates;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldAtom implements IField {

    protected String _patternId;
    protected String _fieldName;

    public FieldAtom(String patternId, String fieldName) {
        _patternId = patternId;
        _fieldName = fieldName;
    }

    @Override
    public String getPatternId() {
        return _patternId;
    }

    @Override
    public String getFieldName() {
        return _fieldName;
    }
}
