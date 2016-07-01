package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.types.IValue;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by wbraik on 08/06/16.
 */
public interface IField {

    String getPatternId();

    /**
     * Returns whether the field can be resolved within a given matchbuffer
     *
     * @param matchBuffer
     * @param currentEvent
     * @return
     */
    boolean isResolvable(ArrayList<Event> matchBuffer, String currentMatchBufferKey, Event currentEvent);

    Optional<IValue<?>> resolve(ArrayList<Event> matchBuffer, String currentMatchBufferKey, Event currentEvent);
}
