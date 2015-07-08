package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.Event;
import fr.labri.patterndetector.EventType;
import fr.labri.patterndetector.Generator;
import fr.labri.patterndetector.rules.FollowedBy;
import fr.labri.patterndetector.rules.Atom;
import fr.labri.patterndetector.rules.IRule;

import java.util.stream.Stream;

/**
 * Created by William Braik on 6/29/2015.
 */
public class TestAutomaton {

    public static void main(String[] args) {

        IRule r = new FollowedBy(new Atom(EventType.EVENT_A), new Atom(EventType.EVENT_B));
        IAutomaton a1 = r.buildAutomaton();

        Stream<Event> events = Generator.generateStream().stream();

        System.out.println(a1.getCurrentState());
        events.forEach(event -> {
            try {
                System.out.println(a1.fire(event.getType()));
            } catch (Exception e) {
                System.err.println(e.getMessage());
                //e.printStackTrace();
            }
        });
    }
}
