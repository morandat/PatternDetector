/**
 * Created by William Braik on 6/23/2015.
 */

import java.util.ArrayList;

/**
 * Generates logs corresponding to various scenarios. *
 */
public final class Generator {

    static private int _t = 0;

    private Generator() {

    }

    public static ArrayList<Event> generateStream() {
        ArrayList<Event> eventStream = new ArrayList<>();

        eventStream.add(new Event(EventType.EVENT_A, _t++));
        eventStream.add(new Event(EventType.EVENT_B, _t++));

        return eventStream;
    }

    // public static ArrayList<Event> generateOtherScenario()

    // public static ArrayList<Event> generateYetAnotherScenario()
}
