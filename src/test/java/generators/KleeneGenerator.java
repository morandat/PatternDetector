package generators;

import fr.labri.patterndetector.executor.Event;
import fr.labri.patterndetector.executor.IEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by wbraik on 11/05/16.
 */
public class KleeneGenerator implements IGenerator {

    public Stream<IEvent> generate() {
        // ... a .. a .. a .. a .. a .. a .. a ...

        Collection<IEvent> events = new ArrayList<>();

        events.add(new Event("x", 1));
        events.add(new Event("a", 2));
        events.add(new Event("c", 3));
        events.add(new Event("a", 4));
        events.add(new Event("a", 5));
        events.add(new Event("y", 6));
        events.add(new Event("a", 7));
        events.add(new Event("b", 8));
        events.add(new Event("x", 9));
        events.add(new Event("c", 10));
        events.add(new Event("a", 11));
        events.add(new Event("end", 12));

        return events.stream();
    }
}
