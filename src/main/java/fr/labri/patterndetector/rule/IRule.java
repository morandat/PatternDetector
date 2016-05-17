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
     * Set the name of the rule.
     *
     * @param name The name of the rule.
     */
    void setName(String name);

    void accept(IRuleVisitor visitor);

    default IAtom getLeftmostAtom() {
        return RuleUtils.getLeftmostAtom(this);
    }
    
    default IAtom getRightmostAtom() {
        return RuleUtils.getRightmostAtom(this);
    }
}
