package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.runtime.predicates.IPredicate;
import fr.labri.patterndetector.runtime.predicates.IStartNacMarker;
import fr.labri.patterndetector.runtime.predicates.IStopNacMarker;

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

    ArrayList<IStartNacMarker> getStartNacMarkers();

    ArrayList<IStopNacMarker> getStopNacMarkers();

    ITransition setMatchBufferKey(String matchBufferKey);

    ITransition setPredicates(ArrayList<IPredicate> predicates);

    ITransition setStartNacMarkers(ArrayList<IStartNacMarker> startNacMarkers);

    ITransition setStopNacMarkers(ArrayList<IStopNacMarker> stopNacMarkers);
}
