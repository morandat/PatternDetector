package fr.labri.patterndetector;

/**
 * Created by William Braik on 6/28/2015.
 */
public interface IEvent {

    public EventType getType();

    public int getTimestamp();
}