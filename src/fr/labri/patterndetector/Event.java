package fr.labri.patterndetector;

/**
 * Created by William Braik on 6/22/2015.
 */
public class Event implements IEvent {

    EventType _type;
    int _timestamp;
    // TODO events have some data (payload) attached.

    public Event(EventType type, int timestamp) {
        _type = type;
        _timestamp = timestamp;
    }

    @Override
    public EventType getType() {
        return _type;
    }

    @Override
    public int getTimestamp() {
        return _timestamp;
    }

    @Override
    public String toString() {
        return _type.toString() + "<" + _timestamp + ">";
    }
}
