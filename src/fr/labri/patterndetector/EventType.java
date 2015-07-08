package fr.labri.patterndetector;

/**
 * Created by William Braik on 6/22/2015.
 */
public enum EventType {

    EVENT_SPECIAL("*"), // Used for negative transitions. Negative transitions negate all the other transitions.
    EVENT_A("A"), EVENT_B("B"), EVENT_C("C");

    private final String _label;

    EventType(String event) {
        _label = event;
    }

    @Override
    public String toString() {
        return _label;
    }
}
