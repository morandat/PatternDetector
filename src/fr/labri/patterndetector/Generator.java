package fr.labri.patterndetector;
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

    public static Collection<Event> generateFollowedBy() {

        // ... a ... b ...

        Collection<Event> eventStream = new ArrayList<>();

        eventStream.add(new Event(EventType.EVENT_B, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_C, _t++));
        eventStream.add(new Event(EventType.EVENT_B, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_C, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_B, _t++));
        eventStream.add(new Event(EventType.EVENT_B, _t++));

        return eventStream;
    }

    public static Collection<Event> generateImmediatelyFollowedBy() {

        // ... a b ...

        Collection<Event> eventStream = new ArrayList<>();

        eventStream.add(new Event(EventType.EVENT_C, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_B, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));

        return eventStream;
    }

    public static Collection<Event> generateKleeneContiguous() {

        // ... a a a a a a a ...

        Collection<Event> eventStream = new ArrayList<>();

        eventStream.add(new Event(EventType.EVENT_B, _t++));
        eventStream.add(new Event(EventType.EVENT_C, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_B, _t++));
        eventStream.add(new Event(EventType.EVENT_C, _t++));

        return eventStream;
    }

    public static Collection<Event> generateKleeneNotContiguous() {

        // ... a .. a .. a .. a .. a .. a .. a ...

        Collection<Event> eventStream = new ArrayList<>();

        eventStream.add(new Event(EventType.EVENT_B, _t++));
        eventStream.add(new Event(EventType.EVENT_C, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_C, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_B, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_B, _t++));
        eventStream.add(new Event(EventType.EVENT_C, _t++));

        return eventStream;
    }
}
