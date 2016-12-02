package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.predicates.IPredicate;
import fr.labri.patterndetector.rule.visitors.IRuleVisitor;
import fr.labri.patterndetector.runtime.predicates.INacBeginMarker;
import fr.labri.patterndetector.runtime.predicates.INacEndMarker;

import java.util.ArrayList;

/**
 * Created by William Braik on 6/25/2015.
 * <p>
 * Operator which binds atoms or sub-patterns together to form patterns.
 */
public interface IRule {

    int getMatchbufferPosition();

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

    ArrayList<INacBeginMarker> getNacBeginMarkers();

    ArrayList<INacEndMarker> getNacEndMarkers();

    Runnable getAction();

    /**
     * SETTERS
     **/
    IRule setMatchbufferPosition(int id);

    /**
     * Set the name of the rule.
     *
     * @param name The name of the rule.
     */
    IRule setName(String name);

    IRule setAction(Runnable run);

    IRule addPredicate(IPredicate predicate);

    IRule setPredicates(ArrayList<IPredicate> predicates);

    IRule addNacBeginMarker(INacBeginMarker startNacMarker);

    IRule setNacBeginMarkers(ArrayList<INacBeginMarker> startNacMarkers);

    IRule addNacEndMarker(INacEndMarker stopNacMarker);

    IRule setNacEndMarkers(ArrayList<INacEndMarker> stopNacMarkers);

    void accept(IRuleVisitor visitor);

    default IAtom getLeftmostAtom() {
        return RuleUtils.getLeftmostAtom(this);
    }

    default IAtom getRightmostAtom() {
        return RuleUtils.getRightmostAtom(this);
    }
}
