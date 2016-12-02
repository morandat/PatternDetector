package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.MatchBuffer;
import fr.labri.patterndetector.runtime.UnknownFieldException;
import fr.labri.patterndetector.runtime.types.IValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldKleeneStaticIndex extends AbstractField implements Serializable {

    protected int _index;

    public FieldKleeneStaticIndex(String fieldName, int fieldPosition, int index) {
        super(fieldName, fieldPosition);
        _index = index;
    }

    int computeIndex(List<Event> events) {
        return _index >= 0 ? _index : events.size() + _index;
    }

    @Override
    public Optional<IValue<?>> resolve(MatchBuffer matchBuffer, Event currentEvent) throws UnknownFieldException {
        List<Event> events = matchBuffer.get(_fieldPosition);
        int index = computeIndex(events);
        if (events.size() >= index)
            return Optional.of(getFieldValue(events.get(index)));
        return Optional.empty();
    }
}
