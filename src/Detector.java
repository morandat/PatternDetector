/**
 * Created by William Braik on 6/22/2015.
 */

import java.util.ArrayList;

/**
 * Parses a stream of events to detect patterns according to a given Spec *
 */
public class Detector {

    // For now the Detector only has a single Rule.
    Rule _rule;

    public Detector(Rule rule) {
        _rule = rule;
    }

    // For now this method returns whether any pattern (defined by the spec) was found within the input event sequence.
    public boolean detect(ArrayList<Event> eventStream) {

        return true;
    }

    // public void addRule()

    // public void removeRule()
}
