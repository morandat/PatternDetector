package fr.labri.patterndetector.types;

import fr.labri.patterndetector.automaton.Transition;

/**
 * Created by wbraik on 09/05/16.
 */
public class LongValue extends AbstractValue<Long> {

    public LongValue(Long value) {
        super(value);
    }

    public LongValue(long value) {
        super(value);
    }
}
