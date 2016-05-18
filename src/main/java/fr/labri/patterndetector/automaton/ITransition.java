package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.executor.predicates.IPredicate;
import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.IntegerValue;

import java.util.ArrayList;

/**
 * Created by William Braik on 7/27/2015.
 * <p>
 * Interface for the transitions of a rule automaton.
 */
public interface ITransition {

    IState getSource();

    IState getTarget();

    String getLabel();

    TransitionType getType();

    ArrayList<IPredicate<IntegerValue>> getPredicates();

    void addPredicate(IPredicate<IntegerValue> predicate);

    void setPredicates(ArrayList<IPredicate<IntegerValue>> predicates);
}
