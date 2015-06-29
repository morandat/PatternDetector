package fr.labri.streamchecking;
/**
 * Created by William Braik on 6/23/2015.
 */

import java.util.ArrayList;
import java.util.Collection;

/**
 * Pre-generates logs according to various scenarios.
 */
public final class Generator {

    static private int _t = 0;

    private Generator() {
    }

    public static Collection<Event> generateStream() {
        Collection<Event> eventStream = new ArrayList<>();

        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_C, _t++));
        eventStream.add(new Event(EventType.EVENT_B, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));

        return eventStream;
    }
}
