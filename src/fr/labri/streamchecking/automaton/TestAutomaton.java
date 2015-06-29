package fr.labri.streamchecking.automaton;

import fr.labri.streamchecking.Event;
import fr.labri.streamchecking.EventType;
import fr.labri.streamchecking.Generator;
import fr.labri.streamchecking.rules.AlwaysFollowedBy;
import fr.labri.streamchecking.rules.Atom;
import fr.labri.streamchecking.rules.IRule;

import java.util.stream.Stream;

/**
 * Created by William Braik on 6/29/2015.
 */
public class TestAutomaton {

    public static void main(String[] args) {

        IRule r = new AlwaysFollowedBy(new Atom(EventType.EVENT_A), new Atom(EventType.EVENT_B));
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