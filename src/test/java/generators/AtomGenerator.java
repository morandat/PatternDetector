package generators;

import fr.labri.patterndetector.executor.Event;
import fr.labri.patterndetector.executor.IEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by wbraik on 11/05/16.
 */
public class AtomGenerator implements IGenerator {

    @Override
    public Stream<IEvent> generate() {
        // a

        Collection<IEvent> events = new ArrayList<>();

        events.add(new Event("b", 1));
        events.add(new Event("a", 2).setData("x", 10));
        events.add(new Event("c", 3));

        return events.stream();
    }
}
