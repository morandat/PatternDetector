package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.EventType;
import fr.labri.patterndetector.automaton.IAutomaton;

/**
 * Created by William Braik on 6/27/2015.
 */
public class Atom extends AbstractRule implements IAtom {

    protected EventType _x;

    public Atom(EventType x) {
        super(RuleType.RULE_ATOM, null);
        _x = x;
    }

    public Atom(EventType x, TimeConstraint tc) {
        super(RuleType.RULE_ATOM, null, tc);
        _x = x;
    }

    @Override
    public EventType getAtom() {
        return _x;
    }

    @Override
    public String toString() {
        return _x.toString();
    }

    @Override
    public IAutomaton buildAutomaton() {
        return null; //TODO
    }
}