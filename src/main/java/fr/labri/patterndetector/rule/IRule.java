package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.executor.predicates.IPredicate;
import fr.labri.patterndetector.rule.visitors.IRuleVisitor;
import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.IntegerValue;

import java.util.ArrayList;

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

    ArrayList<IPredicate> getPredicates();

    Runnable getAction();

    /**
     * Set the name of the rule.
     *
     * @param name The name of the rule.
     */
    IRule setName(String name);

    IRule setAction(Runnable run);

    IRule addPredicate(IPredicate predicate);

    void accept(IRuleVisitor visitor);

    default IAtom getLeftmostAtom() {
        return RuleUtils.getLeftmostAtom(this);
    }

    default IAtom getRightmostAtom() {
        return RuleUtils.getRightmostAtom(this);
    }
}
