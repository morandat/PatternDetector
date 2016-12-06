package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.automaton.ITransition;
import fr.labri.patterndetector.rule.IRule;
import fr.labri.patterndetector.rule.visitors.RuleAutomatonMaker;
import fr.labri.patterndetector.runtime.expressions.*;
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
    private Map<String, DeterministicRunner> _negationRunners; // maps negations to corresponding automata

    public DeterministicRunContext(IState initialState, int matchbufferSize) {
        super();
        _currentState = initialState;
        _matchbuffer = new Matchbuffer(matchbufferSize);
        _negationRunners = new HashMap<>();
    }

    public DeterministicRunContext(IState initialState, Matchbuffer matchbuffer) {
        super();
        _currentState = initialState;
        _matchbuffer = matchbuffer.duplicate(); // FIXME inefficient : matchbuffers might not need to be copied to each negation / subcontext
        _negationRunners = new HashMap<>();
    }

    public DeterministicRunContext(IState initialState, Matchbuffer matchbuffer, Map<String, DeterministicRunner> negationRunners) {
        super();
        _currentState = initialState;
        _matchbuffer = matchbuffer.duplicate();
        _negationRunners = new HashMap<>(); // FIXME inefficient : negations might not need to be copied
        _negationRunners.putAll(negationRunners);
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

    public Map<String, DeterministicRunner> getNegationRunnersMap() {
        return _negationRunners;
    }

    public Collection<DeterministicRunner> getNegationRunners() {
        return _negationRunners.values();
    }

    public void appendEvent(Event event, int patternId) {
        _matchbuffer.append(patternId, event);
    }

    public void clearMatchBuffers() {
        _matchbuffer.clear();
    }

    public void clearNegationRunners() {
        _negationRunners.clear();
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

    public Optional<DeterministicRunner> beginNegation(String negationId, IRule negationRule) {
        if (!_negationRunners.containsKey(negationId)) { // check if this negation was already started (can be the case for kleene transitions)
            IRuleAutomaton negationPowerset = RuleAutomatonMaker.makeAutomaton(negationRule).powerset();
            negationPowerset.validate();

            DeterministicRunner negationRunner = new DeterministicRunner(negationPowerset, _matchbuffer);
            _negationRunners.put(negationId, negationRunner);

            Logger.debug(_contextId + " : negation \"" + negationId + "\" started with context " + negationRunner.getContextId() + " : " + negationRule);

            return Optional.of(negationRunner);
        } else
            return Optional.empty();
    }

    public void endNegation(String negationId) {
        Logger.debug(_contextId + " : negation stopped : " + negationId);

        _negationRunners.remove(negationId);
    }

    public String toString() {
        return _contextId + ":(" + _currentState + ", " + _matchbuffer + ")";
    }
}
