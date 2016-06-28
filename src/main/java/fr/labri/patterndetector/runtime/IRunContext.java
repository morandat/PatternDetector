package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.automaton.IState;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Created by wbraik on 6/13/2016.
 */
public interface IRunContext {
    Stream<IEvent> getMatchBuffers();
}
