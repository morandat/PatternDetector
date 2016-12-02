package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.automaton.ITransition;
import fr.labri.patterndetector.rule.IRule;
import fr.labri.patterndetector.rule.visitors.RuleAutomatonMaker;
import fr.labri.patterndetector.runtime.predicates.*;
import fr.labri.patterndetector.runtime.types.IValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * Created by william.braik on 25/05/2016.
 */
public class DeterministicRunContext extends AbstractRunContext implements Serializable {

    private final Logger Logger = LoggerFactory.getLogger(DeterministicRunContext.class);

    private IState _currentState;
    private Matchbuffer _matchbuffer; // maps pattern ids to corresponding pattern events
    private Map<String, DeterministicRunner> _nacRunners; // maps NAC IDs to corresponding automata

    public DeterministicRunContext(IState initialState, int matchbufferSize) {
        super();
        _currentState = initialState;
        _matchbuffer = new Matchbuffer(matchbufferSize);
        _nacRunners = new HashMap<>();
    }

    public DeterministicRunContext(IState initialState, Matchbuffer matchbuffer) {
        super();
        _currentState = initialState;
        _matchbuffer = matchbuffer.duplicate(); // FIXME inefficient : matchbuffers might not need to be copied to each NAC / subcontext
        _nacRunners = new HashMap<>();
    }

    public DeterministicRunContext(IState initialState, Matchbuffer matchbuffer, Map<String, DeterministicRunner> nacRunners) {
        super();
        _currentState = initialState;
        _matchbuffer = matchbuffer.duplicate();
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

    public Matchbuffer getMatchBuffer() {
        return _matchbuffer;
    }

    public Map<String, DeterministicRunner> getNacRunnersMap() {
        return _nacRunners;
    }

    public Collection<DeterministicRunner> getNacRunners() {
        return _nacRunners.values();
    }

    public void appendEvent(Event event, int patternId) {
        _matchbuffer.append(patternId, event);
    }

    public void clearMatchBuffers() {
        _matchbuffer.clear();
    }

    public void clearNacRunners() {
        _nacRunners.clear();
    }

    public boolean isTransitionValid(ITransition transition, Event current) throws UnknownFieldException {
        for (IPredicate predicate : transition.getPredicates()) {
            if (!evaluatePredicate(predicate, current))
                return false;
        }
        return true;
    }

    public boolean evaluatePredicate(IPredicate predicate, Event current) throws UnknownFieldException {
        IField[] fields = predicate.getFields();
        IValue<?>[] values = new IValue[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Optional<IValue<?>> field = fields[i].resolve(_matchbuffer, current);
            if (!field.isPresent())
                return true;
            values[i] = field.get();
        }
        predicate.eval(values);
        return true;
    }

    public Optional<DeterministicRunner> startNac(String nacId, IRule nacRule) {
        if (!_nacRunners.containsKey(nacId)) { // check if this NAC was already started
            IRuleAutomaton nacPowerset = RuleAutomatonMaker.makeAutomaton(nacRule).powerset();
            nacPowerset.validate();

            DeterministicRunner nacRunner = new DeterministicRunner(nacPowerset, _matchbuffer);
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
        return _contextId + ":(" + _currentState + ", " + _matchbuffer + ")";
    }
}
