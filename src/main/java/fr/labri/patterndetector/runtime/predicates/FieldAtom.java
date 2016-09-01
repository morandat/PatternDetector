package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.types.IValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldAtom extends AbstractField implements Serializable {

    public FieldAtom(String patternId, String fieldName) {
        super(patternId, fieldName);
    }

    @Override
    public boolean isResolvable(ArrayList<Event> matchBuffer, String currentMatchBufferKey, Event currentEvent) {
        return true;
    }

    @Override
    public Optional<IValue<?>> resolve(ArrayList<Event> matchBuffer, String currentMatchBufferKey, Event currentEvent) {
        if (_patternId.equals(currentMatchBufferKey)) {
            return Optional.ofNullable(currentEvent.getPayload().get(_fieldName));
        } else {
            Event event = matchBuffer.get(0); // atoms only have one event in the matchbuffer, so this works
            return Optional.ofNullable(event.getPayload().get(_fieldName));
        }
    }
}
