package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.runtime.expressions.IPredicate;
import fr.labri.patterndetector.rule.INegationBeginMarker;
import fr.labri.patterndetector.rule.INegationEndMarker;

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

    int getMatchbufferPosition();

    ArrayList<IPredicate> getPredicates();

    ArrayList<INegationBeginMarker> getNegationBeginMarkers();

    ArrayList<INegationEndMarker> getNegationEndMarkers();

    ITransition setMatchbufferPosition(int matchbufferPositionKey);

    ITransition setPredicates(ArrayList<IPredicate> predicates);

    ITransition setNegationBeginMarkers(ArrayList<INegationBeginMarker> negationBeginMarkers);

    ITransition setNegationEndMarkers(ArrayList<INegationEndMarker> negationEndMarkers);
}
