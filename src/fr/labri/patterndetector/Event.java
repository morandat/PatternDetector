package fr.labri.patterndetector;

/**
 * Created by William Braik on 6/22/2015.
 */
public class Event implements IEvent {

    String _type;
    int _timestamp;
    // TODO events have some data (aka payload) attached.

    public Event(String type, int timestamp) {
        _type = type;
        _timestamp = timestamp;
    }

    @Override
    public String getType() {
        return _type;
    }

    @Override
    public int getTimestamp() {
        return _timestamp;
    }

    @Override
    public String toString() {
        return _type + "<" + _timestamp + ">";
    }
}
