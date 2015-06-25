/**
 * Created by William Braik on 6/23/2015.
 */

import java.util.ArrayList;

/**
 * Generates logs corresponding to various scenarios. *
 */
public final class Generator {

    private Generator() {

    }

    public static ArrayList<Event> generateStream() {
        ArrayList<Event> eventStream = new ArrayList<Event>();

        eventStream.add(new Event(EventType.EVENT_A, 0, 0));
        eventStream.add(new Event(EventType.EVENT_B, 1, 0));

        return eventStream;
    }

    // public static ArrayList<Event> generateOtherScenario()

    // public static ArrayList<Event> generateYetAnotherScenario()
}
