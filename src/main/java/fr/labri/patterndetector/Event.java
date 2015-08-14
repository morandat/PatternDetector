package fr.labri.patterndetector;

/**
 * Created by William Braik on 6/22/2015.
 */
public class Event implements IEvent {

    String _type;
    long _timestamp;
    // TODO events have some data (aka payload) attached.

    public Event(String type, long timestamp) {
        _type = type;
        _timestamp = timestamp;
    }

    @Override
    public String getType() {
        return _type;
    }

    @Override
    public long getTimestamp() {
        return _timestamp;
    }

    @Override
    public String toString() {
        return _type + "<" + _timestamp + ">";
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Event)) return false;

        Event otherTransition = (Event) other;
        return otherTransition.getType().equals(_type) && otherTransition.getTimestamp() == _timestamp;
    }
}
