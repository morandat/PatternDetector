package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.Matchbuffer;
import fr.labri.patterndetector.runtime.UnknownFieldException;
import fr.labri.patterndetector.runtime.types.IValue;
import fr.labri.patterndetector.runtime.types.LongValue;

import java.util.Optional;

/**
 * Created by morandat on 02/12/2016.
 */
public class FieldCurrentTime implements IField {

    public FieldCurrentTime() {
    }

    @Override
    public Optional<IValue<?>> resolve(Matchbuffer matchbuffer, Event currentEvent) throws UnknownFieldException {
        return Optional.of(LongValue.from(currentEvent.getTimestamp()));
    }
}
