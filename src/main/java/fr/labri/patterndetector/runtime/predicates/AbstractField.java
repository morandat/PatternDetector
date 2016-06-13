package fr.labri.patterndetector.runtime.predicates;

/**
 * Created by wbraik on 6/13/2016.
 */
public abstract class AbstractField implements IField {

    protected FieldType _fieldType;
    protected String _patternId;
    protected String _fieldName;

    public AbstractField(FieldType fieldType, String patternId, String fieldName) {
        _fieldType = fieldType;
        _patternId = patternId;
        _fieldName = fieldName;
    }

    @Override
    public FieldType getFieldType() {
        return _fieldType;
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
