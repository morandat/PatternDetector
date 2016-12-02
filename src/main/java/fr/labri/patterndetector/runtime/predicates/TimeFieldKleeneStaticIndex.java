package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.types.IValue;
import fr.labri.patterndetector.runtime.types.LongValue;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by wbraik on 08/06/16.
 */
public class TimeFieldKleeneStaticIndex extends FieldKleeneStaticIndex {

    public TimeFieldKleeneStaticIndex(String patternId, int index) {
        super(patternId, null, index);
    }

    @Override
    public Optional<IValue<?>> resolve(ArrayList<Event> matchBuffer, String currentMatchBufferKey, Event currentEvent) {
        if (_patternId.equals(currentMatchBufferKey)) {
            if (matchBuffer == null) { // first event of the current kleene seq
                return Optional.of(new LongValue(currentEvent.getTimestamp()));
            } else { // at least one event already in current kleene seq
                int currentIndex = matchBuffer.size(); // index of the currently processed event (not yet appended)

                if (_index == currentIndex) {
                    return Optional.of(new LongValue(currentEvent.getTimestamp()));
                } else {
                    Event event = matchBuffer.get(_index);
                    return Optional.of(new LongValue(event.getTimestamp()));
                }
            }
        } else { // past kleene sequence
            if (_index >= matchBuffer.size()) {
                return Optional.empty();
            } else {
                Event event = matchBuffer.get(_index);
                return Optional.of(new LongValue(event.getTimestamp()));
            }
        }
    }
}
