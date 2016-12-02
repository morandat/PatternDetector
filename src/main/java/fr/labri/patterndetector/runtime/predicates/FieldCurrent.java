package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.MatchBuffer;
import fr.labri.patterndetector.runtime.UnknownFieldException;
import fr.labri.patterndetector.runtime.types.IValue;

import java.util.Optional;

/**
 * Created by morandat on 02/12/2016.
 */
public class FieldCurrent implements IField {

    private final String _fieldName;

    public FieldCurrent(String fieldName) {
        _fieldName = fieldName;
    }

    @Override
    public Optional<IValue<?>> resolve(MatchBuffer matchBuffer, Event currentEvent) throws UnknownFieldException {
        return Optional.of(getFieldValue(currentEvent));
    }

    @Override
    public String getFieldName() {
        return _fieldName;
    }
}
