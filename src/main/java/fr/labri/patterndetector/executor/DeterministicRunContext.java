package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.automaton.ClockGuard;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.executor.predicates.IPredicate;
import fr.labri.patterndetector.types.IValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by william.braik on 25/05/2016.
 */
public class DeterministicRunContext {

    private IState _currentState;
    private Map<String, ArrayList<IEvent>> _matchBuffers; // maps pattern ids to corresponding pattern events
    // TODO private Map<String, Long> _clocks;

    public DeterministicRunContext(IState initialState) {
        _currentState = initialState;
        _matchBuffers = new HashMap<>();
        // TODO _clocks = new HashMap<>();
    }

    public boolean isCurrentStateFinal() {
        return _currentState.isFinal();
    }

    public IState getCurrentState() {
        return _currentState;
    }

    public void updateCurrentState(IState newState) {
        _currentState = newState;
    }

    public ArrayList<IEvent> getMatchBuffer(String patternId) {
        return _matchBuffers.get(patternId);
    }

    public Stream<IEvent> getMatchBuffers() {
        ArrayList<IEvent> matchBuffer = new ArrayList<>();
        _matchBuffers.values().forEach(matchBuffer::addAll);

        return matchBuffer.stream()
                .sorted((e1, e2) -> new Long(e1.getTimestamp()).compareTo(e2.getTimestamp())); // make sure it's sorted chronologically
    }

    public void appendEvent(IEvent event, String patternId) {
        ArrayList<IEvent> matchBuffer = getMatchBuffer(patternId);
        if (matchBuffer == null) {
            matchBuffer = new ArrayList<>();
        }
        matchBuffer.add(event);
        _matchBuffers.put(patternId, matchBuffer);
    }

    public void clearMatchBuffers() {
        _matchBuffers.clear();
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



        if (patternKey.equals(currentMatchBufferKey)) { // Fixme ONLY FOR ATOMS ! Kleene
            return currentEvent.getPayload().get(patternField);
        } else {
            ArrayList<IEvent> matchBuffer = getMatchBuffer(patternKey);
            if (matchBuffer != null) {
                int currentIndex = matchBuffer.size() - 1;
                IEvent firstEvent = matchBuffer.get(currentIndex); // TODO only works for atoms ! for kleene, need index as parameter (ex: k[i])

                return firstEvent.getPayload().get(patternField);
            } else {
                throw new RuntimeException("Could not resolve field : " + field); // FIXME probably should not be a runtime exception
            }
        }
    }

    private boolean isKleene(String patternKey) {
        String ruleType = patternKey.substring(0, 1);
        return ruleType.equals("k");
    }

    public String toString() {
        return "(" + _currentState + ", " + _matchBuffers + ")";
    }
}
