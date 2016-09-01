package fr.labri.patterndetector.types;

import fr.labri.patterndetector.automaton.Transition;

import java.io.Serializable;

/**
 * Created by wbraik on 09/05/16.
 */
public class LongValue extends AbstractValue<Long> implements Serializable {

    public LongValue(Long value) {
        super(value);
    }
}
