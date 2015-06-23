/**
 * Created by William Braik on 6/23/2015.
 */

/** Generates logs corresponding to various scenarios. **/
public final class Generator {

    private Generator() {

    }

    public static Event[] generate() {
        Event[] events = {
                new Event(EventType.EVENT_A, 0, 0),
                new Event(EventType.EVENT_B, 1, 0)
        };

        return events;
    }

    // public static Event[] generateOtherScenario()

    // public static Event[] generateYetAnotherScenario()
}
