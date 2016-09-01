package fr.labri.patterndetector.types;

import java.io.Serializable;

/**
 * Created by wbraik on 09/05/16.
 */
public class StringValue extends AbstractValue<String> implements Serializable {

    public StringValue(String value) {
        super(value);
    }
}
