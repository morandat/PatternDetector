/**
 * Created by William Braik on 6/22/2015.
 */

import java.util.ArrayList;

/**
 * Parses a stream of events to detect patterns according to a given Spec
 */
public class Detector {

    IRule _rule;

    public Detector(IRule rule) {
        _rule = rule;
    }

    // For now this method returns whether any pattern (defined by the spec) was found within the input event sequence.
    public boolean detect(ArrayList<Event> eventStream) {

        System.out.println("Stream : " + eventStream);
        System.out.println("Rule : " + _rule);

        return true;
    }
}
