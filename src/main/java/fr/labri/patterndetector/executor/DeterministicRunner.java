package fr.labri.patterndetector.executor;

/**
 * Created by wbraik on 5/12/2016.
 */

import fr.labri.patterndetector.automaton.ClockGuard;
import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.automaton.ITransition;
import fr.labri.patterndetector.executor.predicates.IPredicate;
import fr.labri.patterndetector.types.IValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Deterministic, reset when no transition found
 */
public final class DeterministicRunner extends AbstractAutomatonRunner {

    private final Logger logger = LoggerFactory.getLogger(DeterministicRunner.class);

    private IState _currentState;
    private Map<String, ArrayList<IEvent>> _matchBuffers; // FIXME make 'context' class containing current state and corresponding matchbuffer
    // FIXME private Map<String, Long> _clocks;

    public DeterministicRunner(IRuleAutomaton automaton) {
        super(automaton);

        _currentState = automaton.getInitialState();
        _matchBuffers = new HashMap<>();
        //_clocks = new HashMap<>();
    }

    @Override
    public void fire(IEvent e) {
        ITransition t = _currentState.pickTransition(e);

        if (t == null) {
            logger.debug("Can't transition (" + e + ")");

            reset();
        } else {
            if (testPredicates(t.getPredicates(), t.getMatchbufferKey(), e)) {
                // TODO check clock constraints

                logger.debug("Transitioning : " + t + " (" + e + ")");

                // Save current event in match buffer or discard it depending on the transition's type
                switch (t.getType()) {
                    case TRANSITION_APPEND:
                        ArrayList<IEvent> matchBuffer = _matchBuffers.get(t.getMatchbufferKey());
                        if (matchBuffer == null) {
                            matchBuffer = new ArrayList<>();
                        }
                        matchBuffer.add(e);
                        _matchBuffers.put(t.getMatchbufferKey(), matchBuffer);

                        // Update event clock
                        // FIXME _clocks.put(e.getType(), e.getTimestamp());
                        break;

                    case TRANSITION_DROP:
                }

                // TODO Any NACs to start ?

                // Update current state
                _currentState = t.getTarget();

                // function callbacks
                _currentState.performActions();

                if (_currentState.isFinal()) {
                    logger.debug("Final state reached");

                    // If the final state has been reached, post the found pattern and reset the automaton
                    Collection<IEvent> pattern = new ArrayList<>();
                    _matchBuffers.values().forEach(pattern::addAll);
                    postPattern(pattern.stream().sorted((e1, e2) -> new Long(e1.getTimestamp()).compareTo(e2.getTimestamp()))
                            .collect(Collectors.toList()));
                    reset();
                }
            }
        }
    }

    public boolean testPredicates(ArrayList<IPredicate> predicates, String currentMatchBufferKey, IEvent currentEvent) {
        // No predicates to test
        if (predicates == null) {
            return true;
        }

        for (IPredicate p : predicates) {
            ArrayList<String> fields = p.getFields();
            ArrayList<IValue<?>> values = new ArrayList<>();

            for (String field : fields) {
                IValue<?> value = resolveField(field, currentMatchBufferKey, currentEvent);
                values.add(value);
            }

            IValue<?>[] valuesArr = new IValue<?>[values.size()];
            valuesArr = values.toArray(valuesArr);
            if (!p.eval(valuesArr))
                return false;
        }

        return true;
    }

    public boolean testClockGuard(long currentTime, ClockGuard clockGuard) {
        // TODO time constraints
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

    private IValue<?> resolveField(String field, String currentMatchBufferKey, IEvent currentEvent) {
        String[] splittedField = field.split("\\.");
        String patternKey = splittedField[0];
        String patternField = splittedField[1];

        if (patternKey.equals(currentMatchBufferKey)) {
            return currentEvent.getPayload().get(patternField);
        } else {
            ArrayList<IEvent> matchBuffer = getMatchBuffer(patternKey);
            if (matchBuffer != null) {
                IEvent firstEvent = matchBuffer.get(0); // TODO only works for atoms ! for kleene, need index as parameter (ex: k[i])
                return firstEvent.getPayload().get(patternField);
            } else {
                throw new RuntimeException("Could not resolve field : " + field); // FIXME probably should not be a runtime exception
            }
        }
    }

    /**
     * Get back to initial state, clear match buffer
     */
    private void reset() {
        _currentState = _automaton.getInitialState();
        _matchBuffers.clear();
        // FIXME _clocks.clear();

        logger.debug("Automaton reset");
    }

    public IState getCurrentState() {
        return _currentState;
    }

    public ArrayList<IEvent> getMatchBuffer(String key) {
        return _matchBuffers.get(key);
    }
}
