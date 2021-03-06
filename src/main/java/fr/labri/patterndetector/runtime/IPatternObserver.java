package fr.labri.patterndetector.runtime;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by wbraik on 10/25/2015.
 * <p>
 * Base interface for pattern observers.
 */
public interface IPatternObserver extends Serializable {

    /**
     * This method must be called by a rule automaton when a pattern is detected.
     *
     * @param pattern The detected pattern.
     */
    void notifyPattern(Collection<Event> pattern);
}
