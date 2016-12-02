package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.Matchbuffer;
import fr.labri.patterndetector.runtime.UnknownFieldException;
import fr.labri.patterndetector.runtime.types.IValue;

import java.util.Map;
import java.util.Optional;

/**
 * Created by wbraik on 08/06/16.
 */
public interface IField {

    Optional<IValue<?>> resolve(Matchbuffer matchbuffer, Event currentEvent) throws UnknownFieldException;

    String getFieldName();

    default IValue<?> getFieldValue(Event event) throws UnknownFieldException {
        Map<String, IValue<?>> payload = event.getPayload();
        if (payload.containsKey(getFieldName()))
            return payload.get(getFieldName());
        throw new UnknownFieldException(event, this);
    }
}
