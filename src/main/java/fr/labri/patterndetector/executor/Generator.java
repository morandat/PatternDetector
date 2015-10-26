package fr.labri.patterndetector.executor;
/**
 * Created by William Braik on 6/23/2015.
 */

import java.util.ArrayList;
import java.util.Collection;

/**
 * Pre-generates logs for various scenarios.
 */
public final class Generator {

    static private long _t;

    private Generator() {
    }

    /**
     * Initialize the generator. Must be called in the beginning of every 'generateX' method.
     */
    private static void init() {
        _t = 0;
    }

    public static Collection<Event> generateFollowedBy() {
        // ... a ... b ...

        init();
        Collection<Event> eventStream = new ArrayList<>();

        eventStream.add(new Event("b", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("c", _t++));
        eventStream.add(new Event("b", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("c", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("b", _t++));
        eventStream.add(new Event("b", _t++));

        return eventStream;
    }

    public static Collection<Event> generateFollowedByContiguous() {
        // ... a b ...

        init();
        Collection<Event> eventStream = new ArrayList<>();

        eventStream.add(new Event("c", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("b", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("b", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("c", _t++));
        eventStream.add(new Event("b", _t++));

        return eventStream;
    }

    public static Collection<Event> generateKleeneContiguous() {
        // ... a a a a a a a ...

        init();
        Collection<Event> eventStream = new ArrayList<>();

        eventStream.add(new Event("b", _t++));
        eventStream.add(new Event("c", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("b", _t++));
        eventStream.add(new Event("c", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("a", _t++));

        return eventStream;
    }

    public static Collection<Event> generateKleene() {
        // ... a .. a .. a .. a .. a .. a .. a ...

        init();
        Collection<Event> eventStream = new ArrayList<>();

        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("x", _t++));
        eventStream.add(new Event("c", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("c", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("c", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("c", _t++));
        eventStream.add(new Event("c", _t++));
        eventStream.add(new Event("b", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("x", _t++));
        eventStream.add(new Event("x", _t++));
        eventStream.add(new Event("b", _t++));

        return eventStream;
    }

    public static Collection<IEvent> generateStuff() {
        init();
        Collection<IEvent> eventStream = new ArrayList<>();

        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("b", _t++));
        eventStream.add(new Event("c", _t++));
        eventStream.add(new Event("a", _t++));

        return eventStream;
    }
}
