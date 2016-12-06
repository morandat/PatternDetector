package fr.labri.patterndetector.runtime.expressions;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.Matchbuffer;
import fr.labri.patterndetector.runtime.UnknownFieldException;
import fr.labri.patterndetector.runtime.types.DoubleValue;
import fr.labri.patterndetector.runtime.types.IValue;
import fr.labri.patterndetector.runtime.types.LongValue;
import fr.labri.patterndetector.runtime.types.StringValue;

import java.util.Optional;

/**
 * Created by morandat on 05/12/2016.
 */
public class Constant implements IField {
    final private IValue<?> _value;

    public Constant(IValue<?> value) {
        _value = value;
    }

    public static Constant from(String value) {
        return new Constant(StringValue.from(value));
    }
    public static Constant from(double value) {
        return new Constant(DoubleValue.from(value));
    }
    public static Constant from(long value) {
        return new Constant(LongValue.from(value));
    }

    @Override
    public Optional<IValue<?>> fetch(Matchbuffer matchbuffer, Event currentEvent) throws UnknownFieldException {
        return Optional.of(_value);
    }
}
