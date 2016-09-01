package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.runtime.predicates.IPredicate;
import fr.labri.patterndetector.runtime.predicates.INacBeginMarker;
import fr.labri.patterndetector.runtime.predicates.INacEndMarker;

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

    String getMatchbufferKey();

    ArrayList<IPredicate> getPredicates();

    ArrayList<INacBeginMarker> getNacBeginMarkers();

    ArrayList<INacEndMarker> getNacEndMarkers();

    ITransition setMatchBufferKey(String matchBufferKey);

    ITransition setPredicates(ArrayList<IPredicate> predicates);

    ITransition setNacBeginMarkers(ArrayList<INacBeginMarker> nacBeginMarkers);

    ITransition setNacEndMarkers(ArrayList<INacEndMarker> nacEndMarkers);
}
