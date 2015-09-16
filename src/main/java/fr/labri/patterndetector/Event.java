package fr.labri.patterndetector;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by William Braik on 6/22/2015.
 */
public class Event implements IEvent {

    String _type;
    long _timestamp;
    Map<String, Integer> _payload; // some data (aka payload) attached to the event.

    public Event(String type, long timestamp) {
        _type = type;
        _timestamp = timestamp;
        _payload = null;
    }

    public Event(String type, long timestamp, Map<String, Integer> payload) {
        _type = type;
        _timestamp = timestamp;
        _payload = payload;
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
    public Map<String, Integer> getPayload() {
        return _payload;
    }

    @Override
    public IEvent addData(String key, Integer value) {
        if (_payload == null) {
            _payload = new HashMap<>();
        }

        _payload.put(key, value);

        return this;
    }

    @Override
    public String toString() {
        return _type + "<" + _timestamp + ">" + _payload;
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
