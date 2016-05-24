package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.automaton.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by wbraik on 5/12/2016.
 */
public abstract class AbstractAutomatonRunner implements IAutomatonRunner {

    // Concrete subclasses should log with their own class names
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected Collection<IPatternObserver> _observers; // Pattern observers to be notified when a pattern is detected.
    protected IRuleAutomaton _automaton;

    public AbstractAutomatonRunner(IRuleAutomaton automaton) {
        _automaton = automaton;
        _observers = new ArrayList<>();
    }

    @Override
    public void registerPatternObserver(IPatternObserver observer) {
        _observers.add(observer);
    }

    @Override
    public void postPattern(Collection<IEvent> pattern) {
        _observers.forEach(observer -> observer.notifyPattern(pattern));
    }
}
