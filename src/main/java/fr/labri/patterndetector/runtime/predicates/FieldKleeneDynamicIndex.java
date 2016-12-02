package fr.labri.patterndetector.runtime.predicates;

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

    protected IntFunction<Integer> _indexFunc;

    public FieldKleeneDynamicIndex(String fieldName, int fieldPosition, IntFunction<Integer> computeIndex) {
        super(fieldName, fieldPosition);
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
