/**
 * Created by William Braik on 6/22/2015.
 */
public enum EventType {

    EVENT_A("A"), EVENT_B("B");

    private final String _event;

    private EventType(String event) {
        _event = event;
    }

    public String toString() {
        return _event;
    }
}
