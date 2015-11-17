/**
 * Created by William Braik on 6/23/2015.
 * <p>
 * Generator for testing patterns.
 */

import fr.labri.patterndetector.executor.Event;
import fr.labri.patterndetector.executor.IEvent;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Pre-generates logs for various scenarios.
 */
public final class Generator {

    private long _t;

    public Generator() {
        _t = 0;
    }

    /**
     * Initialize the generator.
     */
    public void resetTime() {
        _t = 0;
    }

    public Collection<IEvent> generateAtom() {
        // a

        Collection<IEvent> eventStream = new ArrayList<>();

        eventStream.add(new Event("b", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("c", _t++));

        return eventStream;
    }

    public Collection<IEvent> generateFollowedBy() {
        // ... a ... b

        Collection<IEvent> eventStream = new ArrayList<>();

        eventStream.add(new Event("b", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("a", _t++));
        eventStream.add(new Event("c", _t++));
        eventStream.add(new Event("b", _t++));

        return eventStream;
    }

    public Collection<IEvent> generateKleene() {
        // ... a .. a .. a .. a .. a .. a .. a ...

        Collection<IEvent> eventStream = new ArrayList<>();

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
}
