package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.UnknownFieldException;
import fr.labri.patterndetector.runtime.types.IValue;

import java.util.Map;

/**
 * Created by wbraik on 6/13/2016.
 */
public abstract class AbstractField implements IField {

    final protected String _fieldName;
    protected final int _fieldPosition;

    public AbstractField(String fieldName, int fieldPosition) {
        _fieldName = fieldName;
        _fieldPosition = fieldPosition;
    }

    @Override
    public IValue<?> getFieldValue(Event event) throws UnknownFieldException {
        Map<String, IValue<?>> payload = event.getPayload();
        if (payload.containsKey(getFieldName()))
            return payload.get(getFieldName());
        throw new UnknownFieldException(event, this);
    }

    @Override
    public String getFieldName() {
        return _fieldName;
    }
}