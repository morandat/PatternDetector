package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.rule.IRule;
import fr.labri.patterndetector.rule.visitors.RuleAutomatonMaker;
import fr.labri.patterndetector.runtime.predicates.*;
import fr.labri.patterndetector.types.IValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * Created by william.braik on 25/05/2016.
 */
public class DeterministicRunContext {

    private final Logger Logger = LoggerFactory.getLogger(DeterministicRunContext.class);

    private IState _currentState;
    private Map<String, ArrayList<Event>> _matchBuffers; // maps pattern ids to corresponding pattern events
    private Map<String, DeterministicRunner> _nacRunners; // maps NAC IDs to corresponding automata

    public DeterministicRunContext(IState initialState) {
        _currentState = initialState;
        _matchBuffers = new HashMap<>();
        _nacRunners = new HashMap<>();
    }

    public DeterministicRunContext(IState initialState, Map<String, ArrayList<Event>> matchBuffers) {
        _currentState = initialState;
        _matchBuffers = new HashMap<>(); // FIXME inefficient : matchbuffers don't need to be copied to each NAC / subcontext!
        _matchBuffers.putAll(matchBuffers);
        //_matchBuffers = matchBuffers;
        _nacRunners = new HashMap<>();
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

    public Map<String, ArrayList<Event>> getMatchBuffersMap() {
        return _matchBuffers;
    }

    public ArrayList<Event> getMatchBuffer(String patternId) {
        return _matchBuffers.get(patternId);
    }

    public Stream<Event> getMatchBuffersStream() {
        ArrayList<Event> matchBuffer = new ArrayList<>();
        _matchBuffers.values().forEach(matchBuffer::addAll);

        return matchBuffer.stream()
                .sorted((e1, e2) -> new Long(e1.getTimestamp()).compareTo(e2.getTimestamp())); // make sure it's sorted by timestamp
    }

    public Map<String, DeterministicRunner> getNacRunnersMap() {
        return _nacRunners;
    }

    public Collection<DeterministicRunner> getNacRunners() {
        return _nacRunners.values();
    }

    public void appendEvent(Event event, String patternId) {
        ArrayList<Event> matchBuffer = getMatchBuffer(patternId);
        if (matchBuffer == null) {
            matchBuffer = new ArrayList<>();
        }
        matchBuffer.add(event);
        _matchBuffers.put(patternId, matchBuffer);
    }

    public void clearMatchBuffers() {
        _matchBuffers.clear();
    }

    public void clearNacRunners() {
        _nacRunners.clear();
    }

    public boolean testPredicates(ArrayList<IPredicate> predicates, String currentMatchBufferKey, Event currentEvent) {
        // No predicates to test
        if (predicates.isEmpty()) {
            return true;
        }

        for (IPredicate predicate : predicates) {
            ArrayList<IField> fields = predicate.getFields();
            ArrayList<IValue<?>> values = new ArrayList<>();
            boolean skipPredicateCheck = false; // if true, means that current predicate cannot be checked and must be skipped

            for (IField field : fields) {
                Optional<IValue<?>> value;
                String patternId = field.getPatternId();
                ArrayList<Event> matchBuffer = _matchBuffers.get(patternId);

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

    public Optional<DeterministicRunner> startNac(String nacId, IRule nacRule) {
        if (!_nacRunners.containsKey(nacId)) {
            Logger.debug("NAC started : " + nacId + " | " + nacRule);

            IRuleAutomaton nacPowerset = RuleAutomatonMaker.makeAutomaton(nacRule).powerset();
            nacPowerset.validate();

            DeterministicRunner nacRunner = new DeterministicRunner(nacPowerset, _matchBuffers);
            _nacRunners.put(nacId, nacRunner);

            return Optional.of(nacRunner);
        } else
            return Optional.empty();
    }

    public void stopNac(String nacId) {
        Logger.debug("NAC stopped : " + nacId);

        _nacRunners.remove(nacId);
    }

    public String toString() {
        return "(" + _currentState + ", " + _matchBuffers + ")";
    }
}
