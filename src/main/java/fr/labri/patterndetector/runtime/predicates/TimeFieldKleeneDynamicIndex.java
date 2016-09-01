package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.LongValue;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.IntFunction;

/**
 * Created by wbraik on 08/06/16.
 */
public class TimeFieldKleeneDynamicIndex extends FieldKleeneDynamicIndex {

    public TimeFieldKleeneDynamicIndex(String patternId, IntFunction<Integer> indexFunc) {
        super(patternId, null, indexFunc);
    }

    @Override
    public Optional<IValue<?>> resolve(ArrayList<Event> matchBuffer, String currentMatchBufferKey, Event currentEvent) {
        if (matchBuffer == null) { // first event of the current kleene seq
            return Optional.of(new LongValue(currentEvent.getTimestamp()));
        } else { // at least one event already in current kleene seq
            int currentIndex = matchBuffer.size(); // index of the currently processed event (not yet appended)
            int fetchIndex = _indexFunc.apply(currentIndex);

            if (fetchIndex == currentIndex) {
                return Optional.of(new LongValue(currentEvent.getTimestamp()));
            } else {
                Event event = matchBuffer.get(fetchIndex);
                return Optional.of(new LongValue(event.getTimestamp()));
            }
        }
    }
}
