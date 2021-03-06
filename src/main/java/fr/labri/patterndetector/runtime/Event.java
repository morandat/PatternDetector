package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.runtime.expressions.IValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by William Braik on 6/22/2015.
 */
public class Event implements Serializable {

    private String _type; // Type of event (ex: View, Add...)
    private long _timestamp; // Time of occurence of the event
    private String _pid; // ID of the producer of the event
    private Map<String, IValue<?>> _payload; // Some data (or payload) attached to the event. Maps field names to values

    // FIXME constructor with dummy pid for convenience only
    public Event(String type, long timestamp) {
        _type = type;
        _timestamp = timestamp;
        _pid = "pid";
        _payload = new HashMap<>();
    }

    public Event(String type, long timestamp, String pid) {
        _type = type;
        _timestamp = timestamp;
        _pid = pid;
        _payload = new HashMap<>();
    }

    public Event(String type, long timestamp, Map<String, IValue<?>> payload) {
        _type = type;
        _timestamp = timestamp;
        _pid = "pid";
        _payload = payload;
    }

    public Event(String type, long timestamp, String pid, Map<String, IValue<?>> payload) {
        _type = type;
        _timestamp = timestamp;
        _pid = pid;
        _payload = payload;
    }

    public String getType() {
        return _type;
    }

    public long getTimestamp() {
        return _timestamp;
    }

    public String getPid() {
        return _pid;
    }

    public Map<String, IValue<?>> getPayload() {
        return _payload;
    }

    public Event setData(String key, Integer value) {
        _payload.put(key, IValue.LongValue.from(value.longValue()));

        return this;
    }

    public Event setData(String key, Long value) {
        _payload.put(key, IValue.LongValue.from(value));

        return this;
    }

    public Event setData(String key, String value) {
        _payload.put(key, IValue.StringValue.from(value));

        return this;
    }

    public Event setData(String key, Double value) {
        _payload.put(key, IValue.DoubleValue.from(value));

        return this;
    }

    @Override
    public String toString() {
        return _type + "<" + _timestamp + ">" + (_payload == null ? "" : " " + _payload);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Event)) return false;

        Event otherEvent = (Event) other;
        return otherEvent.getType().equals(_type) && otherEvent.getTimestamp() == _timestamp;
    }
}
