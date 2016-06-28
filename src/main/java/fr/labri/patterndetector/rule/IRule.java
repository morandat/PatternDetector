package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.predicates.IPredicate;
import fr.labri.patterndetector.rule.visitors.IRuleVisitor;
import fr.labri.patterndetector.runtime.predicates.IStartNacMarker;
import fr.labri.patterndetector.runtime.predicates.IStopNacMarker;

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

    ArrayList<IStartNacMarker> getStartNacMarkers();

    ArrayList<IStopNacMarker> getStopNacMarkers();

    Runnable getAction();

    /**
     * Set the name of the rule.
     *
     * @param name The name of the rule.
     */
    IRule setName(String name);

    IRule setAction(Runnable run);

    IRule addPredicate(IPredicate predicate);

    IRule setPredicates(ArrayList<IPredicate> predicates);

    IRule addStartNacMarker(IStartNacMarker startNacMarker);

    IRule setStartNacMarkers(ArrayList<IStartNacMarker> startNacMarkers);

    IRule addStopNacMarker(IStopNacMarker stopNacMarker);

    IRule setStopNacMarkers(ArrayList<IStopNacMarker> stopNacMarkers);

    void accept(IRuleVisitor visitor);

    default IAtom getLeftmostAtom() {
        return RuleUtils.getLeftmostAtom(this);
    }

    default IAtom getRightmostAtom() {
        return RuleUtils.getRightmostAtom(this);
    }
}
