package fr.labri.patterndetector.runtime.predicates;

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

    public Constant(String value) {
        this(new StringValue(value));
    }

    public Constant(double value) {
        this(new DoubleValue(value));
    }

    public Constant(long value) {
        this(new LongValue(value));
    }

    @Override
    public Optional<IValue<?>> resolve(Matchbuffer matchbuffer, Event currentEvent) throws UnknownFieldException {
        return Optional.of(_value);
    }
}
