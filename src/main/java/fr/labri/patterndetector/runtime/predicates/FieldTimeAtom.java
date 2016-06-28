package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.IEvent;
import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.LongValue;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldTimeAtom extends FieldAtom {

    public FieldTimeAtom(String patternId) {
        super(patternId, null);
    }

    @Override
    public Optional<IValue<?>> resolve(ArrayList<IEvent> matchBuffer, String currentMatchBufferKey, IEvent currentEvent) {
        if (_patternId.equals(currentMatchBufferKey)) {
            return Optional.of(new LongValue(currentEvent.getTimestamp()));
        } else {
            IEvent event = matchBuffer.get(0); // atoms only have one event in the matchbuffer, so this works
            return Optional.of(new LongValue(event.getTimestamp()));
        }
    }
}
