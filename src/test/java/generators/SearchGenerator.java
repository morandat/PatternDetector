package generators;

import fr.labri.patterndetector.executor.Event;
import fr.labri.patterndetector.executor.IEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by wbraik on 11/05/16.
 */
public class SearchGenerator implements IGenerator {

    @Override
    public Stream<IEvent> generate() {
        // q .. f .. f .. a

        Collection<IEvent> events = new ArrayList<>();

        events.add(new Event("q", 1).setData("url", "q").setData("ref", "cdiscount.com"));
        events.add(new Event("f", 2).setData("url", "f1").setData("ref", "q"));
        events.add(new Event("f", 3).setData("url", "f2").setData("ref", "q"));
        events.add(new Event("a", 4).setData("url", "a").setData("ref", "f2"));

        // TODO add referrer / url constraints

        return events.stream();
    }
}
