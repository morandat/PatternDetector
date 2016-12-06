package fr.labri.patterndetector.runtime.expressions;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.Matchbuffer;
import fr.labri.patterndetector.runtime.UnknownFieldException;
import fr.labri.patterndetector.runtime.types.IValue;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * Created by wbraik on 08/06/16.
 */
public interface IField extends Serializable {

    Optional<IValue<?>> fetch(Matchbuffer matchbuffer, Event currentEvent) throws UnknownFieldException;

    default IValue<?> getFieldValue(Event event, String fieldName) throws UnknownFieldException {
        Map<String, IValue<?>> payload = event.getPayload();
        if (payload.containsKey(fieldName))
            return payload.get(fieldName);
        throw new UnknownFieldException(event, this);
    }
}
