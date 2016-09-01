package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.LongValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by wbraik on 08/06/16.
 */
public class TimeFieldAtom extends FieldAtom implements Serializable {

    public TimeFieldAtom(String patternId) {
        super(patternId, null);
    }

    @Override
    public Optional<IValue<?>> resolve(ArrayList<Event> matchBuffer, String currentMatchBufferKey, Event currentEvent) {
        if (_patternId.equals(currentMatchBufferKey)) {
            return Optional.of(new LongValue(currentEvent.getTimestamp()));
        } else {
            Event event = matchBuffer.get(0); // atoms only have one event in the matchbuffer, so this works
            return Optional.of(new LongValue(event.getTimestamp()));
        }
    }
}
