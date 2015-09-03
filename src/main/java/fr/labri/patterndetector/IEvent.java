package fr.labri.patterndetector;

import java.util.Map;

/**
 * Created by William Braik on 6/28/2015.
 */
public interface IEvent {

    String getType();

    long getTimestamp();

    Map<String, Integer> getPayload();

    /**
     * Add a key-valued data field to the event's payload
     *
     * @param key   The key
     * @param value The value
     * @return The event itself
     */
    IEvent addData(String key, Integer value);
}
