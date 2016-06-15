package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.IEvent;
import fr.labri.patterndetector.types.IValue;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldAtom extends AbstractField {

    public FieldAtom(String patternId, String fieldName) {
        super(FieldType.FIELD_ATOM, patternId, fieldName);
    }

    @Override
    public boolean isResolvable(ArrayList<IEvent> matchBuffer, String currentMatchBufferKey, IEvent currentEvent) {
        return true;
    }

    @Override
    public Optional<IValue<?>> resolve(ArrayList<IEvent> matchBuffer, String currentMatchBufferKey, IEvent currentEvent) {
        if (_patternId.equals(currentMatchBufferKey)) {
            if (currentEvent.getPayload().get(_fieldName) == null) {
                return Optional.empty();
            } else {
                return Optional.of(currentEvent.getPayload().get(_fieldName));
            }
        } else {
            IEvent event = matchBuffer.get(0); // atoms only have one event in the matchbuffer, so this works

            return Optional.ofNullable(event.getPayload().get(_fieldName));
        }
    }
}
