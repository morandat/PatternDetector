package fr.labri.patterndetector.runtime.expressions;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.UnknownFieldException;
import fr.labri.patterndetector.runtime.types.IValue;

import java.util.Map;

/**
 * Created by wbraik on 6/13/2016.
 */
public abstract class AbstractField implements IField {

    final protected String _fieldName;

    public AbstractField(String fieldName) {
        _fieldName = fieldName;
    }

    public IValue<?> getFieldValue(Event event) throws UnknownFieldException {
        Map<String, IValue<?>> payload = event.getPayload();
        if (payload.containsKey(getFieldName()))
            return payload.get(getFieldName());
        throw new UnknownFieldException(event, this);
    }

    public String getFieldName() {
        return _fieldName;
    }
}