package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.automaton.Transition;
import fr.labri.patterndetector.rule.IRule;
import fr.labri.patterndetector.rule.visitors.RuleAutomatonMaker;
import fr.labri.patterndetector.runtime.predicates.*;
import fr.labri.patterndetector.runtime.types.IValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by william.braik on 25/05/2016.
 */
public class DeterministicRunContext extends AbstractRunContext implements Serializable {

    private final Logger Logger = LoggerFactory.getLogger(DeterministicRunContext.class);

    private IState _currentState;
    private MatchBuffer _matchBuffer; // maps pattern ids to corresponding pattern events
    private Map<String, DeterministicRunner> _nacRunners; // maps NAC IDs to corresponding automata

    public DeterministicRunContext(IState initialState, int matchBufferSize) {
        super();
        _currentState = initialState;
        _matchBuffer = new MatchBuffer(matchBufferSize);
        _nacRunners = new HashMap<>();
    }

    public DeterministicRunContext(IState initialState, MatchBuffer matchBuffer) {
        super();
        _currentState = initialState;
        _matchBuffer = matchBuffer.duplicate(); // FIXME inefficient : matchbuffers might not need to be copied to each NAC / subcontext
        _nacRunners = new HashMap<>();
    }

    public DeterministicRunContext(IState initialState, MatchBuffer matchBuffer, Map<String, DeterministicRunner> nacRunners) {
        super();
        _currentState = initialState;
        _matchBuffer = matchBuffer.duplicate();
        _nacRunners = new HashMap<>(); // FIXME inefficient : NACS might not need to be copied
        _nacRunners.putAll(nacRunners);
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

    public MatchBuffer getMatchBuffer() {
        return _matchBuffer;
    }

    public Map<String, DeterministicRunner> getNacRunnersMap() {
        return _nacRunners;
    }

    public Collection<DeterministicRunner> getNacRunners() {
        return _nacRunners.values();
    }

    public void appendEvent(Event event, int patternId) {
        _matchBuffer.append(patternId, event);
    }

    public void clearMatchBuffers() {
        _matchBuffer.clear();
    }

    public void clearNacRunners() {
        _nacRunners.clear();
    }

    public boolean isTransitionValid(Transition transition, Event current) throws UnknownFieldException {
        for (IPredicate predicate: transition.getPredicates()) {
            if (!evaluatePredicate(predicate, current))
                return false;
        }
        return true;
    }

    public boolean evaluatePredicate(IPredicate predicate, Event current) throws UnknownFieldException {
        IField[] fields = predicate.getFields();
        IValue<?>[] values = new IValue[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Optional<IValue<?>> field = fields[i].resolve(_matchBuffer, current);
            if (!field.isPresent())
                return true;
            values[i] = field.get();
        }
        predicate.eval(values);
        return true;
    }

    public boolean testPredicates(ArrayList<IPredicate> predicates, int currentMatchBufferKey, Event currentEvent) {
        // No predicates to test
        if (predicates.isEmpty()) {
            return true;
        }

        for (IPredicate predicate : predicates) {
            IField[] fields = predicate.getFields();
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
        if (!_nacRunners.containsKey(nacId)) { // check if this NAC was already started
            IRuleAutomaton nacPowerset = RuleAutomatonMaker.makeAutomaton(nacRule).powerset();
            nacPowerset.validate();

            DeterministicRunner nacRunner = new DeterministicRunner(nacPowerset, _matchBuffer);
            _nacRunners.put(nacId, nacRunner);

            Logger.debug(_contextId + " : NAC \"" + nacId + "\" started with context " + nacRunner.getContextId() + " : " + nacRule);

            return Optional.of(nacRunner);
        } else
            return Optional.empty();
    }

    public void stopNac(String nacId) {
        Logger.debug(_contextId + " : NAC stopped : " + nacId);

        _nacRunners.remove(nacId);
    }

    public String toString() {
        return _contextId + ":(" + _currentState + ", " + _matchBuffer + ")";
    }
}
