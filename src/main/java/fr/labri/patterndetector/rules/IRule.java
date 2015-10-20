package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.IRuleAutomaton;

/**
 * Created by William Braik on 6/25/2015.
 * <p>
 * Operator which binds atoms or sub-patterns together to form patterns.
 */
public interface IRule {

    /**
     * Get the name of the rule.
     *
     * @return The name of the rule.
     */
    String getName();

    /**
     * Get the symbol of the operator represented by the rule.
     *
     * @return The symbol of the operator represented by the rule.
     */
    String getSymbol();

    /**
     * Get the rule's automaton. This method lazily calls buildAutomaton() if needed.
     *
     * @return The rule's automaton.
     */
    IRuleAutomaton getAutomaton();

    /**
     * Get the time constraint specified for the rule.
     *
     * @return The time constraint specification of the rule.
     */
    TimeConstraint getTimeConstraint();

    /**
     * Get the connection state's label from the rule's automaton.
     * The connection state is the state which is used by operators (such as FollowedBy) as a connection point between
     * the current rule's automaton and the following pattern's automaton.
     *
     * @return The connection state's label of the rule's automaton.
     */
    String getConnectionStateLabel();

    /**
     * Set the name of the rule.
     *
     * @param name The name of the rule.
     */
    void setName(String name);

    /**
     * Specify a time constraint for the rule.
     *
     * @param timeConstraint The time constraint to specify for the rule.
     * @return The rule itself.
     */
    IRule setTimeConstraint(TimeConstraint timeConstraint);

    /**
     * Specify a time constraint for the rule.
     *
     * @param value The value of the time constraint to specify for the rule.
     * @return The rule itself.
     */
    IRule setTimeConstraint(int value);
}
