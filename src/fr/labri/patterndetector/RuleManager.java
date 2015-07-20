package fr.labri.patterndetector;
/**
 * Created by William Braik on 6/22/2015.
 */

import fr.labri.patterndetector.automaton.IAutomaton;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.rules.IRule;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Parses a stream of events to detect patterns according to a given Rule
 */
public class RuleManager {

    IRule _rule;

    public RuleManager(IRule rule) {
        _rule = rule;
    }

    public void detect(Collection<Event> events) {

        System.out.println("Stream : " + events);
        System.out.println("Rule : " + _rule);

        IAutomaton automaton = _rule.getAutomaton();
        System.out.println(automaton.getCurrentState());

        events.stream().forEach(event -> {
            try {
                IState s = automaton.fire(event);
                // If the automaton has reached its final state, return the buffer and reset the automaton.
                if (s.isFinal()) {
                    post(automaton.getBuffer());
                    automaton.reset();
                }
                System.out.println(s);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                //e.printStackTrace();
            }
        });
    }

    public void post(Collection<IEvent> pattern) {
        System.out.println("PATTERN FOUND ! " + pattern);
    }
}
