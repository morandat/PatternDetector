package fr.labri.patterndetector.runtime.predicates;

/**
 * Created by wbraik on 6/13/2016.
 */
public abstract class AbstractField implements IField {

    protected String _patternId;
    protected String _fieldName;

    public AbstractField(String patternId, String fieldName) {
        _patternId = patternId;
        _fieldName = fieldName;
    }

    @Override
    public String getPatternId() {
        return _patternId;
    }
}
