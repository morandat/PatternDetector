package fr.labri.patterndetector.executor.predicates;

import fr.labri.patterndetector.rule.RuleType;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldKleene extends FieldAtom implements IFieldKleene {

    private int _index;

    public FieldKleene(String patternId, String fieldName, int index) {
        super(patternId, fieldName);
        _index = index;
        _patternType = RuleType.KLEENE;
    }

    @Override
    public int getIndex() {
        return _index;
    }
}
