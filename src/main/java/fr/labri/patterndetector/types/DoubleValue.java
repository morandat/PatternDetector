package fr.labri.patterndetector.types;

import java.io.Serializable;

/**
 * Created by wbraik on 09/05/16.
 */
public class DoubleValue extends AbstractValue<Double> implements Serializable {

    public DoubleValue(Double value) {
        super(value);
    }
}
