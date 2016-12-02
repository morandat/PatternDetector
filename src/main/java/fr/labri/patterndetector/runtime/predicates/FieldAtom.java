package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.Matchbuffer;
import fr.labri.patterndetector.runtime.UnknownFieldException;
import fr.labri.patterndetector.runtime.types.IValue;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldAtom extends AbstractField implements Serializable {

    public FieldAtom(String fieldName, int fieldPosition) {
        super(fieldName, fieldPosition);
    }

    @Override
    public Optional<IValue<?>> resolve(Matchbuffer matchbuffer, Event currentEvent) throws UnknownFieldException {
        return Optional.of(getFieldValue(matchbuffer.get(_fieldPosition).get(0)));
    }
}
