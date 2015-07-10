package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.EventType;

/**
 * Created by William Braik on 6/27/2015.
 */
public interface IAtom extends IRule {

    public EventType getEventType();
}
