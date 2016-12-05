package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.Matchbuffer;
import fr.labri.patterndetector.runtime.UnknownFieldException;
import fr.labri.patterndetector.runtime.types.IValue;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldKleeneStaticIndex extends AbstractField implements Serializable {

    protected int _index;
    private final int _fieldPosition;

    public FieldKleeneStaticIndex(int fieldPosition, String fieldName, int index) {
        super(fieldName);
        _index = index;
        _fieldPosition = fieldPosition;
    }

    int computeIndex(List<Event> events) {
        return _index >= 0 ? _index : events.size() + _index;
    }

    @Override
    public Optional<IValue<?>> resolve(Matchbuffer matchbuffer, Event currentEvent) throws UnknownFieldException {
        List<Event> events = matchbuffer.get(_fieldPosition);
        if (events == null)
            return Optional.empty();
        int index = computeIndex(events);
        if (index < events.size())
            return Optional.of(getFieldValue(events.get(index)));
        return Optional.empty();
    }
}
