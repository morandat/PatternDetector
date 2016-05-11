package generators;

import fr.labri.patterndetector.executor.Event;
import fr.labri.patterndetector.executor.IEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by wbraik on 11/05/16.
 */
public class KleeneGenerator2 implements IGenerator {

    @Override
    public Stream<IEvent> generate() {
        // ... a .. a .. a .. a .. a .. a .. a ...

        Collection<IEvent> events = new ArrayList<>();

        events.add(new Event("x", 1));
        events.add(new Event("a", 2));
        events.add(new Event("c", 3));
        events.add(new Event("b", 4));
        events.add(new Event("x", 5));
        events.add(new Event("x", 6));
        events.add(new Event("x", 7));
        events.add(new Event("x", 8));
        events.add(new Event("a", 9));
        events.add(new Event("y", 10));
        events.add(new Event("y", 11));
        events.add(new Event("y", 12));
        events.add(new Event("y", 13));
        events.add(new Event("y", 14));
        events.add(new Event("y", 15));
        events.add(new Event("y", 16));
        events.add(new Event("b", 17));
        events.add(new Event("x", 18));
        events.add(new Event("c", 19));
        events.add(new Event("end", 20));

        return events.stream();
    }
}
