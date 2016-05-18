package fr.labri.patterndetector.types;

import fr.labri.patterndetector.automaton.Transition;

/**
 * Created by wbraik on 09/05/16.
 */
public class IntegerValue extends AbstractValue<Integer> {

    public IntegerValue(Integer value) {
        super(value);
    }

    public IntegerValue(int value) {
        super(value);
    }
}
