package fr.labri.patterndetector;
/**
 * Created by William Braik on 6/22/2015.
 */

import fr.labri.patterndetector.automaton.IAutomaton;
import fr.labri.patterndetector.rules.IRule;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Parses a stream of events to detect patterns according to a given Rule
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

        try {
            IAutomaton a1 = _rule.buildAutomaton();
            System.out.println(a1.getCurrentState());

            events.stream().forEach(event -> {
                try {
                    System.out.println(a1.fire(event.getType()));
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    //e.printStackTrace();
                }
            });

            System.out.println("\nOutput : " + a1);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return true;
    }
}
