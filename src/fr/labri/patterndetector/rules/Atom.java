package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.EventType;
import fr.labri.patterndetector.automaton.*;

/**
 * Created by William Braik on 6/27/2015.
 */
public class Atom extends AbstractRule implements IAtom {

    protected EventType _x;

    public Atom(EventType x) {
        super(RuleType.RULE_ATOM, null);
        _x = x;
    }

    @Override
    public EventType getEventType() {
        return _x;
    }

    @Override
    public String toString() {
        return _x.toString();
    }
}
