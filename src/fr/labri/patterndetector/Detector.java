package fr.labri.patterndetector;
/**
 * Created by William Braik on 6/22/2015.
 */

import fr.labri.patterndetector.rules.IRule;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Parses a stream of events to detect patterns according to a given Spec
 */
public class Detector {

    IRule _rule;

    public Detector(IRule rule) {
        _rule = rule;
    }

    // For now this method returns whether any pattern (defined by the rule) was found within the input event sequence.
    public boolean detect(Collection<Event> events) {

        System.out.println("Stream : " + events);
        System.out.println("Rule : " + _rule);

        return true;
    }
}
