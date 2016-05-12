package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.automaton.ClockGuard;
import fr.labri.patterndetector.automaton.IState;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by wbraik on 5/12/2016.
 */
public interface IAutomatonRunner {
    /**
     * Get the current states in the automaton.
     *
     * @return The current states in the automaton.
     */
    ArrayList<IState> getCurrentStates();

    /**
     * From current state(s), try to find a transition that matches the event, and update the current state(s)
     *
     * @param e the event to read
     */
    void fire(IEvent e);

    /**
     * Get the current match buffer
     *
     * @return The match buffer
     */
    Collection<IEvent> getMatchBuffer();

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
    void patternDetected(Collection<IEvent> pattern);

    boolean testClockGuard(long currentTime, ClockGuard clockGuard);
}