package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.automaton.IRuleAutomaton;

import java.util.Collection;

/**
 * Created by wbraik on 5/12/2016.
 */
public interface IAutomatonRunner {
    /**
     * From current state(s), try to find a transition that matches the event, and update the current state(s)
     *
     * @param e the event to read
     */
    void fire(Event e);

    IRunContext getContext();

    IRuleAutomaton getAutomaton();

    /**
     * Register a pattern observer.
     *
     * @param observer The pattern observer to register.
     */
    void registerPatternObserver(IPatternObserver observer);

    /**
     * Action performed when a pattern is found (i.e. when the final state is reached)
     *
     * @param pattern The detected pattern.
     */
    void postPattern(Collection<Event> pattern);

    /**
     * Reset context
     */
    void resetContext();

    /**
     * Get context ID
     *
     * @return
     */
    public long getContextId();
}
