package fr.labri.patterndetector.executor.predicates;

import fr.labri.patterndetector.rule.RuleType;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldAtom implements IField {

    protected String _patternId;
    protected String _fieldName;
    protected RuleType _patternType;

    public FieldAtom(String patternId, String fieldName) {
        _patternId = patternId;
        _fieldName = fieldName;
        _patternType = RuleType.ATOM;
    }

    @Override
    public String getPatternId() {
        return _patternId;
    }

    @Override
    public String getFieldName() {
        return _fieldName;
    }

    @Override
    public RuleType getPatternType() {
        return _patternType;
    }
}
