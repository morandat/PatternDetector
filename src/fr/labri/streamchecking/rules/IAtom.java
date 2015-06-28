package fr.labri.streamchecking.rules;

import fr.labri.streamchecking.EventType;

/**
 * Created by William Braik on 6/27/2015.
 */
public interface IAtom extends IRule {

    public EventType getAtom();
}
