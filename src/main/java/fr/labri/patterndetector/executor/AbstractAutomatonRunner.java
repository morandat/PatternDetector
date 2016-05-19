package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.automaton.*;
import fr.labri.patterndetector.executor.predicates.IPredicate;
import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.IntegerValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wbraik on 5/12/2016.
 */
public abstract class AbstractAutomatonRunner implements IAutomatonRunner {

    // Concrete subclasses should log with their own class names
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected ArrayList<IState> _currentStates;
    //protected ArrayList<IEvent> _matchBuffer; // Events matching the current pattern
    protected Map<String, ArrayList<IEvent>> _matchBuffers;
    // FIXME protected Map<String, Long> _clocks;
    protected Collection<IPatternObserver> _observers; // Pattern observers to be notified when a pattern is detected.
    protected IRuleAutomaton _automaton;

    public AbstractAutomatonRunner(IRuleAutomaton automaton) {
        _automaton = automaton;
        _currentStates = new ArrayList<>();
        //_matchBuffer = new ArrayList<>();
        _matchBuffers = new HashMap<>();
        //_clocks = new HashMap<>();
        _observers = new ArrayList<>();

        _currentStates.add(_automaton.getInitialState());
    }

    @Override
    public ArrayList<IState> getCurrentStates() {
        return _currentStates;
    }

    @Override
    public Collection<IEvent> getMatchBuffer(String key) {
        return _matchBuffers.get(key);
    }

    @Override
    public void registerPatternObserver(IPatternObserver observer) {
        _observers.add(observer);
    }

    @Override
    public void postPattern(Collection<IEvent> pattern) {
        _observers.forEach(observer -> observer.notifyPattern(pattern));
    }

    @Override
    public boolean testPredicates(ArrayList<IPredicate<IntegerValue>> predicates, String currentMatchBufferKey, IEvent currentEvent) {
        // No predicates to test
        if (predicates == null) {
            return true;
        }

        for (IPredicate<IntegerValue> p : predicates) {
            ArrayList<String> fields = p.getFields();
            ArrayList<IntegerValue> values = new ArrayList<>();

            for (String field : fields) {
                IntegerValue value = resolveField(field, currentMatchBufferKey, currentEvent);
                values.add(value);
            }

            if (!p.eval(values))
                return false;
        }

        return true;
    }

    @Override
    public IntegerValue resolveField(String field, String currentMatchBufferKey, IEvent currentEvent) {
        String[] splittedField = field.split("\\.");
        String patternKey = splittedField[0];
        String patternField = splittedField[1];

        if (patternKey.equals(currentMatchBufferKey)) {
            return (IntegerValue) currentEvent.getPayload().get(patternField);
        } else {
            return (IntegerValue) _matchBuffers.get(patternKey).get(0).getPayload().get(patternField);
        }
    }

    @Override
    public boolean testClockGuard(long currentTime, ClockGuard clockGuard) {
        // TODO
        /*if (clockGuard == null) {
            return true;
        } else if (_clocks.get(clockGuard.getEventType()) == null) {
            return true;
        } else {
            long timeLast = _clocks.get(clockGuard.getEventType());
            long timeSinceLast = currentTime - timeLast;

            if (clockGuard.isLowerThan()) {
                return timeSinceLast <= clockGuard.getValue();
            } else {
                return timeSinceLast > clockGuard.getValue();
            }
        }*/

        return true;
    }
}
