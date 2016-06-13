package fr.labri.patterndetector.runtime.predicates;

import com.sun.istack.internal.Nullable;
import fr.labri.patterndetector.runtime.IEvent;
import fr.labri.patterndetector.types.IValue;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by wbraik on 08/06/16.
 */
public interface IField {

    FieldType getFieldType();

    String getPatternId();

    String getFieldName();

    /**
     * Returns whether the field can be resolved within a given matchbuffer
     *
     * @param matchBuffer
     * @param currentEvent
     * @return
     */
    boolean isResolvable(@Nullable ArrayList<IEvent> matchBuffer, String currentMatchBufferKey, IEvent currentEvent);

    Optional<IValue<?>> resolve(@Nullable ArrayList<IEvent> matchBuffer, String currentMatchBufferKey, IEvent currentEvent);
}
