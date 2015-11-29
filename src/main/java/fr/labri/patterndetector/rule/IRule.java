package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.rule.visitors.IRuleVisitor;

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
     * Get the time constraint specified for the rule.
     *
     * @return The time constraint specification of the rule.
     */
    TimeConstraint getTimeConstraint();

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
    // FIXME should probably not be here. Create interface for rules that can be time-constrained (Or cannot be)
    IRule setTimeConstraint(TimeConstraint timeConstraint);

    /**
     * Specify a time constraint for the rule.
     *
     * @param value The value of the time constraint to specify for the rule.
     * @return The rule itself.
     */
    IRule setTimeConstraint(int value);

    void accept(IRuleVisitor visitor);

    /**
     * @return The corresponding powerset automaton of this automaton.
     */
    default IAtom getLeftmostAtom() {
        return RuleUtils.getLeftmostAtom(this);
    }

    /**
     * @return A copy of this automaton.
     */
    default IAtom getRightmostAtom() {
        return RuleUtils.getRightmostAtom(this);
    }
}
