package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.AutomatonUtils;
import fr.labri.patterndetector.automaton.IRuleAutomaton;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by William Braik on 6/25/2015.
 * <p>
 * Operator which binds atoms or sub-patterns together to form patterns.
 */
public abstract class AbstractRule implements IRule {

    protected String _name;
    protected String _symbol;
    protected TimeConstraint _timeConstraint;
    protected String _connectionStateLabel;
    protected IRuleAutomaton _automaton;

    public AbstractRule(String symbol) {
        _symbol = symbol;
        _timeConstraint = null;
        _connectionStateLabel = null;
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
    public String getSymbol() {
        return _symbol;
    }

    @Override
    public TimeConstraint getTimeConstraint() {
        return _timeConstraint;
    }

    @Override
    public String getConnectionStateLabel() {
        return _connectionStateLabel;
    }

    @Override
    public IRule setTimeConstraint(TimeConstraint timeConstraint) {
        _timeConstraint = timeConstraint;

        // If the rule already has an automaton, it needs to be reconstructed to apply clock constraints
        // TODO actually we don't need to reconstruct the automaton, just to call createClockConstraints() so make it an abstract method
        if (_automaton != null) {
            try {
                // TODO createClockConstraints()
                buildAutomaton();
            } catch (Exception e) {
                System.err.println("An error occurred while building the automaton (" + e.getMessage() + ")");
            }
        }

        return this;
    }

    @Override
    public IRule setTimeConstraint(int value) {
        return setTimeConstraint(new TimeConstraint(value));
    }

    @Override
    public final IRuleAutomaton getAutomaton() {
        if (_automaton == null) {
            try {
                buildAutomaton();
            } catch (Exception e) {
                System.err.println("An error occurred while building the automaton (" + e.getMessage() + ")");
            }
        }

        return _automaton;
    }

    // TODO put this method in an AutomatonFactory class ?
    public abstract void buildAutomaton() throws Exception; // TODO RuleException
}
