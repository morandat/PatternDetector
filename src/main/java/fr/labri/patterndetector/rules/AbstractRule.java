package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.IRuleAutomaton;

/**
 * Created by William Braik on 6/25/2015.
 */
public abstract class AbstractRule implements IRule {

    protected String _name;
    protected RuleType _type;
    protected String _symbol;
    protected TimeConstraint _timeConstraint;
    protected SelectionPolicy _selectionPolicy;
    protected IRuleAutomaton _automaton;

    public AbstractRule(RuleType type, String symbol) {
        _type = type;
        _symbol = symbol;
        _selectionPolicy = null;
        _timeConstraint = null;
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
        return _timeConstraint;
    }

    @Override
    public SelectionPolicy getSelectionPolicy() {
        return _selectionPolicy;
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
    public IRule setTimeConstraint(int value, boolean transitive) {
        return setTimeConstraint(new TimeConstraint(value, transitive));
    }

    @Override
    public IRule setSelectionPolicy(SelectionPolicy selectionPolicy) {
        _selectionPolicy = selectionPolicy;

        return this;
    }

    /**
     * Lazily calls buildAutomaton()
     *
     * @return The automaton that executes the rule.
     */
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

    /**
     * Must be called by the RuleManager before adding a rule.
     */
    // TODO maybe put this method in an Automaton Factory class ?
    public abstract void buildAutomaton() throws Exception; // TODO RuleException
}
