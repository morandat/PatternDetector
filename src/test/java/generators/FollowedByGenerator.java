package generators;

import fr.labri.patterndetector.executor.Event;
import fr.labri.patterndetector.executor.IEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by wbraik on 11/05/16.
 */
public class FollowedByGenerator implements IGenerator {

    @Override
    public Stream<IEvent> generate() {
        // ... a ... b

        Collection<IEvent> events = new ArrayList<>();

        events.add(new Event("b", 1));
        events.add(new Event("a", 2));
        events.add(new Event("x", 3));
        events.add(new Event("a", 4));
        events.add(new Event("c", 5));
        events.add(new Event("b", 6));

        return events.stream();
    }
}
