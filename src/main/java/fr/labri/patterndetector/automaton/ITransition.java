package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.runtime.predicates.IPredicate;

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

    ArrayList<IPredicate> getPredicates();

    String getMatchbufferKey();

    ITransition setPredicates(ArrayList<IPredicate> predicates);

    ITransition setMatchBufferKey(String matchBufferKey);
}
