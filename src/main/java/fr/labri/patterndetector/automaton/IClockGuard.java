package fr.labri.patterndetector.automaton;

/**
 * Created by william.braik on 08/07/2015.
 */
public interface IClockGuard {

    int getValue();

    boolean isLowerThan();

    String getEventType();
}
