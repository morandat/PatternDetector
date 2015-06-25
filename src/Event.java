/**
 * Created by William Braik on 6/22/2015.
 */
public class Event {

    EventType _type;
    int _timestamp;

    public Event(EventType type, int timestamp) {
        _type = type;
        _timestamp = timestamp;
    }

    @Override
    public String toString() {
        return _type.toString() + "<" + _timestamp + ">";
    }
}
