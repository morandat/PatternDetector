package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.automaton.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by wbraik on 5/12/2016.
 */
public abstract class AbstractAutomatonRunner implements IAutomatonRunner, Serializable {

    // Concrete subclasses should log with their own class names
    protected final Logger Logger = LoggerFactory.getLogger(getClass());

    protected Collection<IPatternObserver> _observers; // Pattern observers to be notified when a pattern is detected.
    protected IRuleAutomaton _automaton;
    protected boolean _isNegation;

    public AbstractAutomatonRunner(IRuleAutomaton automaton) {
        _automaton = automaton;
        _observers = new ArrayList<>();
        _isNegation = false;
    }

    public AbstractAutomatonRunner(IRuleAutomaton automaton, boolean isNegation) {
        _automaton = automaton;
        _observers = new ArrayList<>();
        _isNegation = isNegation;
    }

    @Override
    public IRuleAutomaton getAutomaton() {
        return _automaton;
    }

    @Override
    public void registerPatternObserver(IPatternObserver observer) {
        _observers.add(observer);
    }

    @Override
    public void postPattern(Collection<Event> pattern) {
        _observers.forEach(observer -> observer.notifyPattern(pattern));
    }
}
