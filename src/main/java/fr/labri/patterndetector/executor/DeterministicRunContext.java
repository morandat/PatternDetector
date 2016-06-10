package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.automaton.ClockGuard;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.executor.predicates.*;
import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.IntegerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;
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
        if (predicates == null || predicates.isEmpty()) {
            return true;
        }

        for (IPredicate p : predicates) {
            ArrayList<IField> fields = p.getFields();
            ArrayList<IValue<?>> values = new ArrayList<>();

            for (IField field : fields) {
                IValue<?> value;

                // FIXME(bad code)
                if (field instanceof FieldKleeneDynamicIndex)
                    value = resolveFieldKleeneDynamicIndex((FieldKleeneDynamicIndex) field, currentMatchBufferKey, currentEvent);
                else if (field instanceof FieldKleeneStaticIndex)
                    value = resolveFieldKleeneStaticIndex((FieldKleeneStaticIndex) field, currentMatchBufferKey, currentEvent);
                else if (field instanceof FieldKleeneRange)
                    value = resolveFieldKleeneRange((FieldKleeneRange) field, currentMatchBufferKey, currentEvent);
                else if (field instanceof FieldAtom)
                    value = resolveFieldAtom((FieldAtom) field, currentMatchBufferKey, currentEvent);
                else
                    throw new RuntimeException("Field type not supported");

                if (value != null)
                    values.add(value);
                else
                    return false;
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

    private IValue<?> resolveFieldAtom(FieldAtom field, String currentMatchBufferKey, IEvent currentEvent) {
        String patternKey = field.getPatternId();
        String patternField = field.getFieldName();

        if (patternKey.equals(currentMatchBufferKey)) {
            return currentEvent.getPayload().get(patternField);
        } else {
            ArrayList<IEvent> matchBuffer = getMatchBuffer(patternKey);
            IEvent event = matchBuffer.get(0); // atoms only have one event in the matchbuffer, so this works

            return event.getPayload().get(patternField);
        }
    }

    private IValue<?> resolveFieldKleeneDynamicIndex(FieldKleeneDynamicIndex field, String currentMatchBufferKey, IEvent currentEvent) {
        String patternKey = field.getPatternId();
        String patternField = field.getFieldName();
        IntFunction<Integer> indexFunc = field.getIndexFunc();

        if (patternKey.equals(currentMatchBufferKey)) {
            ArrayList<IEvent> matchBuffer = getMatchBuffer(patternKey);

            if (matchBuffer == null) { // first event of the current kleene seq
                int currentIndex = 0;
                int fetchIndex = indexFunc.apply(currentIndex);

                if (fetchIndex == currentIndex) {
                    return currentEvent.getPayload().get(patternField);
                } else {
                    return null; // TODO when can't resolve i-1 yet
                }
            } else { // at least one event already in current kleene seq
                int currentIndex = matchBuffer.size(); // index of the currently processed event (not yet appended)
                int fetchIndex = indexFunc.apply(currentIndex);

                if (fetchIndex < 0) {
                    return null; // TODO can't resolve this index yet
                } else if (currentIndex == fetchIndex) {
                    return currentEvent.getPayload().get(patternField);
                } else {
                    IEvent event = matchBuffer.get(fetchIndex);
                    return event.getPayload().get(patternField);
                }
            }
        } else {
            throw new RuntimeException("Dynamic index selectors on past kleene sequences not supported");
        }
    }

    private IValue<?> resolveFieldKleeneStaticIndex(FieldKleeneStaticIndex field, String currentMatchBufferKey, IEvent currentEvent) {
        String patternKey = field.getPatternId();
        String patternField = field.getFieldName();
        int index = field.getIndex();

        if (patternKey.equals(currentMatchBufferKey)) {
            ArrayList<IEvent> matchBuffer = getMatchBuffer(patternKey);

            if (matchBuffer == null) { // first event of the current kleene seq
                int currentIndex = 0;

                if (index == 0) {
                    return currentEvent.getPayload().get(patternField);
                } else {
                    return null; // TODO can't resolve this index because there's only one event
                }
            } else { // at least one event already in current kleene seq
                int currentIndex = matchBuffer.size(); // index of the currently processed event (not yet appended)

                IEvent event = matchBuffer.get(index);
                return event.getPayload().get(patternField);
            }
        } else { // past kleene sequence
            ArrayList<IEvent> matchBuffer = getMatchBuffer(patternKey);

            IEvent event = matchBuffer.get(index);
            return event.getPayload().get(patternField);
        }
    }

    private IValue<?> resolveFieldKleeneRange(FieldKleeneRange field, String currentMatchBufferKey, IEvent currentEvent) {
        String patternKey = field.getPatternId();
        String patternField = field.getFieldName();
        int startIndex = field.getStartIndex();
        int endIndex = field.getEndIndex();

        ArrayList<IEvent> matchBuffer = getMatchBuffer(patternKey);

        if (startIndex < 0 || startIndex > matchBuffer.size() - 1 || endIndex < 0 || endIndex > matchBuffer.size() - 1
                || startIndex > endIndex)
            throw new RuntimeException("Can't resolve field in kleene : bad range spec");

        throw new UnsupportedOperationException("Not implemented yet"); // TODO
    }

    public String toString() {
        return "(" + _currentState + ", " + _matchBuffers + ")";
    }
}
