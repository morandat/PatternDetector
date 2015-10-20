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
     * Add new data to the event's payload
     *
     * @param key   The field name
     * @param value The value
     * @return The event itself
     */
    IEvent setData(String key, Integer value);
}
