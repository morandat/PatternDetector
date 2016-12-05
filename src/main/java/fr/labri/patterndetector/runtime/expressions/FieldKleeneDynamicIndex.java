package fr.labri.patterndetector.runtime.expressions;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.Matchbuffer;
import fr.labri.patterndetector.runtime.UnknownFieldException;
import fr.labri.patterndetector.runtime.types.IValue;

import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldKleeneDynamicIndex extends AbstractField {

    private final IntFunction<Integer> _indexFunc;
    private final int _fieldPosition;

    public FieldKleeneDynamicIndex(int fieldPosition, String fieldName, IntFunction<Integer> computeIndex) {
        super(fieldName);
        _fieldPosition = fieldPosition;
        _indexFunc = computeIndex;
    }

    @Override
    public Optional<IValue<?>> resolve(Matchbuffer matchbuffer, Event currentEvent) throws UnknownFieldException {
        List<Event> events = matchbuffer.get(_fieldPosition);
        int size = events.size();
        int index = _indexFunc.apply(size);
        if (size >= index)
            return Optional.of(getFieldValue(events.get(index)));
        return Optional.empty();
    }
}
