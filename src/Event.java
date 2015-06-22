/**
 * Created by William Braik on 6/22/2015.
 */
public class Event {

    EventType _type;
    int _timestamp;
    int _clientId;

    public Event(EventType type, int timestamp, int clientId) {
        _type = type;
        _timestamp = timestamp;
        _clientId = clientId;
    }
}
