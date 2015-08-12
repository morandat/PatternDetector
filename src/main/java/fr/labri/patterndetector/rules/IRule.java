package fr.labri.patterndetector.rules;
/**
 * Created by William Braik on 6/25/2015.
 */

import fr.labri.patterndetector.automaton.IRuleAutomaton;

/**
 * Query that describes the stream patterns the user is interested in.
 */
public interface IRule {

    String getName();

    void setName(String name);

    RuleType getType();

    String getSymbol();

    IRuleAutomaton getAutomaton();

    TimeConstraint getTimeConstraint();

    SelectionPolicy getSelectionPolicy();

    IRule setTimeConstraint(TimeConstraint timeConstraint);

    IRule setTimeConstraint(int value);

    IRule setSelectionPolicy(SelectionPolicy selectionPolicy);
}
