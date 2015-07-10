package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.EventType;
import fr.labri.patterndetector.automaton.IAutomaton;

/**
 * Created by william.braik on 08/07/2015.
 */
public class AtomNot extends AbstractRule implements IAtom {

    protected EventType _x; // This atom represents all event types except this one (!x)

    public AtomNot(EventType x) {
        super(RuleType.RULE_ATOM_NOT, null);
        _x = x;
    }

    public AtomNot(EventType x, TimeConstraint tc) {
        super(RuleType.RULE_ATOM_NOT, null, tc);
        _x = x;
    }

    @Override
    public EventType getEventType() {
        return _x;
    }

    @Override
    public String toString() {
        return "!" + _x.toString();
    }

    @Override
    public IAutomaton buildAutomaton() {
        return null; //TODO
    }
}
