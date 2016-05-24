package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.automaton.ClockGuard;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.executor.predicates.IPredicate;
import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.IntegerValue;

import java.util.ArrayList;
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
    void fire(IEvent e);

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
    void postPattern(Collection<IEvent> pattern);
}
