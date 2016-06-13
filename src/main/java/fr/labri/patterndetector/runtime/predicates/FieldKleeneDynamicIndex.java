package fr.labri.patterndetector.runtime.predicates;

import com.sun.istack.internal.Nullable;
import fr.labri.patterndetector.runtime.IEvent;
import fr.labri.patterndetector.types.IValue;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.IntFunction;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldKleeneDynamicIndex extends AbstractField {

    private IntFunction<Integer> _indexFunc;

    public FieldKleeneDynamicIndex(String patternId, String fieldName, IntFunction<Integer> indexFunc) {
        super(FieldType.FIELD_KLEENE_DYNAMIC_INDEX, patternId, fieldName);
        _indexFunc = indexFunc;
    }

    public IntFunction<Integer> getIndexFunc() {
        return _indexFunc;
    }

    @Override
    public boolean isResolvable(ArrayList<IEvent> matchBuffer, String currentMatchBufferKey, IEvent currentEvent) {
        if (_patternId.equals(currentMatchBufferKey)) { // currently processed kleene
            int currentIndex; // index of the currently processed event (not yet appended)
            if (matchBuffer == null)
                currentIndex = 0;
            else
                currentIndex = matchBuffer.size();

            int fetchIndex = _indexFunc.apply(currentIndex);
            if (fetchIndex < 0)
                return false;
        } else {
            throw new UnsupportedOperationException("Dynamic index selectors not supported on past kleene sequences");
        }

        return true;
    }

    @Override
    public Optional<IValue<?>> resolve(ArrayList<IEvent> matchBuffer, String currentMatchBufferKey, IEvent currentEvent) {
        if (matchBuffer == null) { // first event of the current kleene seq
            return Optional.ofNullable(currentEvent.getPayload().get(_fieldName));
        } else { // at least one event already in current kleene seq
            int currentIndex = matchBuffer.size(); // index of the currently processed event (not yet appended)
            int fetchIndex = _indexFunc.apply(currentIndex);

            if (fetchIndex == currentIndex) {
                return Optional.ofNullable(currentEvent.getPayload().get(_fieldName));
            } else {
                IEvent event = matchBuffer.get(fetchIndex);
                return Optional.ofNullable(event.getPayload().get(_fieldName));
            }
        }
    }
}
