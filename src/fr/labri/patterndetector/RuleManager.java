package fr.labri.patterndetector;
/**
 * Created by William Braik on 6/22/2015.
 */

import fr.labri.patterndetector.automaton.AutomatonUtils;
import fr.labri.patterndetector.automaton.IAutomaton;
import fr.labri.patterndetector.rules.IRule;

import java.util.Collection;

/**
 * Parses a stream of events to detect patterns corresponding to a given Rule
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

        System.out.println("POWERSET AUTOMATON : " + AutomatonUtils.powerset(automaton));

        // TODO automaton.check() : checks whether the automaton is correct (i.e. only has deterministic transitions, etc.)
        // Check that the automaton is correct

        if (automaton != null) {
            events.stream().forEach(event -> {
                try {
                    automaton.fire(event);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            });
        } else {
            System.err.println("Invalid rule : " + _rule);
        }
    }
}
