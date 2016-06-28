package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.IEvent;
import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.LongValue;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.IntFunction;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldTimeKleeneDynamicIndex extends FieldKleeneDynamicIndex {

    public FieldTimeKleeneDynamicIndex(String patternId, IntFunction<Integer> indexFunc) {
        super(patternId, null, indexFunc);
    }

    @Override
    public Optional<IValue<?>> resolve(ArrayList<IEvent> matchBuffer, String currentMatchBufferKey, IEvent currentEvent) {
        if (matchBuffer == null) { // first event of the current kleene seq
            return Optional.of(new LongValue(currentEvent.getTimestamp()));
        } else { // at least one event already in current kleene seq
            int currentIndex = matchBuffer.size(); // index of the currently processed event (not yet appended)
            int fetchIndex = _indexFunc.apply(currentIndex);

            if (fetchIndex == currentIndex) {
                return Optional.of(new LongValue(currentEvent.getTimestamp()));
            } else {
                IEvent event = matchBuffer.get(fetchIndex);
                return Optional.of(new LongValue(event.getTimestamp()));
            }
        }
    }
}
