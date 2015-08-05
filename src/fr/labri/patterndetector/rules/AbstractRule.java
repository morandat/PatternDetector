package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.IRuleAutomaton;

/**
 * Created by William Braik on 6/25/2015.
 */
public abstract class AbstractRule implements IRule {

    protected String _name;
    protected RuleType _type;
    protected String _symbol;
    protected TimeConstraint _tc;
    protected SelectionPolicy _sp;
    protected IRuleAutomaton _automaton;

    public AbstractRule(RuleType type, String symbol) {
        _type = type;
        _symbol = symbol;
        _sp = null;
        _tc = null;
        _automaton = null;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }

    @Override
    public RuleType getType() {
        return _type;
    }

    @Override
    public String getSymbol() {
        return _symbol;
    }

    @Override
    public TimeConstraint getTimeConstraint() {
        return _tc;
    }

    @Override
    public SelectionPolicy getSelectionPolicy() {
        return _sp;
    }

    @Override
    public IRule setTimeConstraint(TimeConstraint tc) {
        _tc = tc;

        return this;
    }

    @Override
    public IRule setSelectionPolicy(SelectionPolicy sp) {
        _sp = sp;

        return this;
    }

    @Override
    public IRuleAutomaton getAutomaton() {
        return _automaton;
    }
}
