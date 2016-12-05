package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.Matchbuffer;
import fr.labri.patterndetector.runtime.UnknownFieldException;
import fr.labri.patterndetector.runtime.types.IValue;
import fr.labri.patterndetector.runtime.types.LongValue;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldKleeneStaticIndexTime implements IField {

    protected int _fieldPosition;
    protected int _index;

    public FieldKleeneStaticIndexTime(int fieldPosition, int index) {
        _fieldPosition = fieldPosition;
        _index = index;
    }

    int computeIndex(List<Event> events) {
        return _index >= 0 ? _index : events.size() + _index;
    }

    @Override
    public Optional<IValue<?>> resolve(Matchbuffer matchbuffer, Event currentEvent) throws UnknownFieldException {
        List<Event> events = matchbuffer.get(_fieldPosition);
        int index = computeIndex(events);
        if (events.size() >= index)
            return Optional.of(new LongValue(events.get(index).getTimestamp()));
        return Optional.empty();
    }
}
