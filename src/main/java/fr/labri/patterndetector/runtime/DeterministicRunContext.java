package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.rule.IRule;
import fr.labri.patterndetector.runtime.predicates.*;
import fr.labri.patterndetector.types.IValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * Created by william.braik on 25/05/2016.
 */
public class DeterministicRunContext implements IRunContext {

    private final Logger logger = LoggerFactory.getLogger(DeterministicRunContext.class);

    private IState _currentState;
    private Map<String, ArrayList<IEvent>> _matchBuffers; // maps pattern ids to corresponding pattern events

    public DeterministicRunContext(IState initialState) {
        _currentState = initialState;
        _matchBuffers = new HashMap<>();
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

    @Override
    public Stream<IEvent> getMatchBuffers() {
        ArrayList<IEvent> matchBuffer = new ArrayList<>();
        _matchBuffers.values().forEach(matchBuffer::addAll);

        return matchBuffer.stream()
                .sorted((e1, e2) -> new Long(e1.getTimestamp()).compareTo(e2.getTimestamp())); // make sure it's sorted by timestamp
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

        for (IPredicate predicate : predicates) {
            ArrayList<IField> fields = predicate.getFields();
            ArrayList<IValue<?>> values = new ArrayList<>();
            boolean skipPredicateCheck = false; // if true, means that current predicate cannot be checked and must be skipped

            for (IField field : fields) {
                Optional<IValue<?>> value;
                String patternId = field.getPatternId();
                ArrayList<IEvent> matchBuffer = _matchBuffers.get(patternId);

                skipPredicateCheck = !field.isResolvable(matchBuffer, currentMatchBufferKey, currentEvent);

                if (!skipPredicateCheck) {
                    value = field.resolve(_matchBuffers.get(field.getPatternId()), currentMatchBufferKey, currentEvent);

                    if (value.isPresent()) // value was retrieved successfully
                        values.add(value.get());
                    else
                        return false;
                }
            }

            if (!skipPredicateCheck) { // current predicate must be skipped
                IValue<?>[] valuesArr = new IValue<?>[values.size()];
                valuesArr = values.toArray(valuesArr);
                if (!predicate.eval(valuesArr))
                    return false;
            }
        }

        return true;
    }

    public String toString() {
        return "(" + _currentState + ", " + _matchBuffers + ")";
    }
}
