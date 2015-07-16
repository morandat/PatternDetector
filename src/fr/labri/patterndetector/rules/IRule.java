package fr.labri.patterndetector.rules;
/**
 * Created by William Braik on 6/25/2015.
 */

import fr.labri.patterndetector.automaton.IAutomaton;

/**
 * Query/Rule that describes the patterns (complex events) we are interested in.
 */
public interface IRule {

    public RuleType getType();

    public String getSymbol();

    public IAutomaton buildAutomaton() throws Exception;

    public TimeConstraint getTimeConstraint();

    public SelectionPolicy getSelectionPolicy();

    public IRule setTimeConstraint(TimeConstraint tc);

    public IRule setSelectionPolicy(SelectionPolicy sp);

    // TODO getOutputSpecification();

    // TODO getName();
}
