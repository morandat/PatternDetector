package fr.labri.patterndetector.executor.predicates;

import fr.labri.patterndetector.rule.RuleType;

/**
 * Created by wbraik on 08/06/16.
 */
public interface IField {

    String getPatternId();

    String getFieldName();

    RuleType getPatternType();
}
