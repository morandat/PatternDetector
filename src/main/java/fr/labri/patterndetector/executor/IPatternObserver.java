package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.automaton.IRuleAutomaton;

import java.util.Collection;

/**
 * Created by Ma on 10/25/2015.
 * <p>
 * Base interface for pattern observers.
 */
public interface IPatternObserver {

    /**
     * This method must be called by a rule automaton when a pattern is detected.
     *
     * @param ruleAutomaton The rule automaton which detected the pattern.
     * @param pattern       The detected pattern.
     */
    void notifyPattern(IRuleAutomaton ruleAutomaton, Collection<IEvent> pattern);
}
