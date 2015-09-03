package fr.labri.patterndetector;
/**
 * Created by William Braik on 6/23/2015.
 */

import java.util.ArrayList;
import java.util.Collection;

/**
 * Pre-generates logs for various scenarios.
 */
public final class Generator {

    static private long _t = 0;

    private Generator() {
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

        eventStream.add(new Event("a", _t++).addData("age", 20).addData("height", 175));
        eventStream.add(new Event("a", _t++).addData("age", 13).addData("height", 200));
        eventStream.add(new Event("a", _t++).addData("age", 10));
        /*eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("b", _t++));
        eventStream.add(new Event("x", _t++));
        eventStream.add(new Event("y", _t++));
        eventStream.add(new Event("c", _t++));
        eventStream.add(new Event("b", _t++));
        eventStream.add(new Event("z", _t++));
        eventStream.add(new Event("c", _t++));
        eventStream.add(new Event("y", _t++));*/

        return eventStream;
    }

    /**
     * Initialize the generator. Must be called at the start of every generation method.
     */
    private static void init() {
        _t = 0;
    }
}
