package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.automaton.exception.AutomatonException;
import fr.labri.patterndetector.executor.IEvent;
import fr.labri.patterndetector.executor.IPatternObserver;
import fr.labri.patterndetector.rules.IRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by William Braik on 6/28/2015.
 * <p>
 * A rule's automaton.
 * A type of timed automaton. Contains clocks for each event type.
 */

public class RuleAutomaton implements IRuleAutomaton {

    private final Logger logger = LoggerFactory.getLogger(RuleAutomaton.class);
    protected IRule _rule;
    protected IState _initialState;
    protected IState _finalState;
    protected Map<String, IState> _states;
    protected IState _currentState;
    protected ArrayList<IEvent> _matchBuffer; // Events matching the current pattern
    protected Map<String, Long> _clocks;
    protected Collection<IPatternObserver> _observers; // Pattern observers to be notified when a pattern is detected.

    public RuleAutomaton(IRule rule) {
        _rule = rule;
        _states = new HashMap<>();
        _matchBuffer = new ArrayList<>();
        _clocks = new HashMap<>();
        _observers = new ArrayList<>();
    }

    @Override
    public IRule getRule() {
        return _rule;
    }

    @Override
    public IState getCurrentState() {
        return _currentState;
    }

    @Override
    public IState getInitialState() {
        return _initialState;
    }

    @Override
    public IState getStateByLabel(String label) {
        if (State.LABEL_INITIAL.equals(label)) return _initialState;
        else if (State.LABEL_FINAL.equals(label)) return _finalState;
        else return _states.get(label);
    }

    @Override
    public Map<String, IState> getStates() {
        return _states;
    }

    @Override
    public IState getFinalState() {
        return _finalState;
    }

    @Override
    public Collection<IEvent> getMatchBuffer() {
        return _matchBuffer;
    }

    @Override
    public Collection<ITransition> getTransitions() {
        Set<ITransition> transitions = new HashSet<>();
        _states.values().forEach(state -> transitions.addAll(state.getTransitions()));

        return transitions;
    }

    @Override
    public void setInitialState(IState s) throws AutomatonException {
        if (_initialState != null) {
            throw new AutomatonException(this, "Initial state already set");
        }
        s.setLabel(State.LABEL_INITIAL);
        s.setInitial(true);
        s.setAutomaton(this);
        _initialState = s;
    }

    @Override
    public void addState(IState s) {
        s.setLabel(Integer.toString(_states.size()));
        s.setInitial(false);
        s.setFinal(false);
        s.setAutomaton(this);
        _states.put(s.getLabel(), s);
    }

    @Override
    public void setFinalState(IState s) throws AutomatonException {
        if (_finalState != null) {
            throw new AutomatonException(this, "Final state already set");
        }
        s.setLabel(State.LABEL_FINAL);
        s.setFinal(true);
        s.setAutomaton(this);
        _finalState = s;
    }

    @Override
    public void fire(IEvent e) throws Exception {
        if (_initialState != null) {
            // Initialize current state if needed
            if (_currentState == null) {
                _currentState = _initialState;
            }

            ITransition t = _currentState.pickTransition(e);

            // If there is a transition, check its clock guards if any
            if (t != null
                    && testClockGuard(e.getTimestamp(), t.getClockConstraint())
                    && testPredicates(e.getPayload(), t.getPredicates())) {

                logger.debug("Transitioning : " + t + " (" + e + ")");

                // Action to perform on the transition
                switch (t.getType()) {
                    case TRANSITION_APPEND:
                        _matchBuffer.add(e);
                        // Update event clock
                        _clocks.put(e.getType(), e.getTimestamp());
                        break;
                    case TRANSITION_DROP:
                }

                // Update current state
                _currentState = t.getTarget();

                if (_currentState.isFinal()) {
                    logger.debug("Final state reached");

                    // If the final state has been reached, post the found pattern and reset the automaton
                    patternDetected(_matchBuffer);
                    reset();
                }
            } else {
                logger.debug("Can't transition (" + e + ")");

                reset();
            }

        } else {
            throw new Exception("Initial state not set");
        }
    }

    @Override
    public void reset() {
        _currentState = _initialState;
        _matchBuffer.clear();
        _clocks.clear();

        logger.debug("Automaton reset");
    }

    @Override
    public void registerPatternObserver(IPatternObserver observer) {
        _observers.add(observer);
    }

    @Override
    public void patternDetected(Collection<IEvent> pattern) {
        _observers.forEach(observer -> observer.notifyPattern(this, pattern)); // Notify observers
    }

    /**
     * Returns true if the clock guard passes, false otherwise
     */
    public boolean testClockGuard(long currentTime, ClockGuard clockGuard) {
        if (clockGuard == null) {
            return true;
        } else if (_clocks.get(clockGuard.getEventType()) == null) {
            return false;
        } else {
            long timeLast = _clocks.get(clockGuard.getEventType());
            long timeSinceLast = currentTime - timeLast;

            if (clockGuard.getLowerThan()) {
                return timeSinceLast <= clockGuard.getValue();
            } else {
                return timeSinceLast > clockGuard.getValue();
            }
        }
    }

    /**
     * Returns true if the predicates pass, false otherwise
     */
    public boolean testPredicates(Map<String, Integer> payload, Map<String, Predicate<Integer>> predicates) {
        // No predicates to test
        if (predicates == null) {
            return true;
        }

        // Some predicates to test, but no data
        if (payload == null) {
            return false;
        }

        // Some predicates to test, and some data
        for (Map.Entry<String, Predicate<Integer>> entry : predicates.entrySet()) {
            String field = entry.getKey();
            Predicate<Integer> predicate = entry.getValue();
            Integer value = payload.get(field);

            /* If the payload misses a field that is required by a predicate, then ''value'' will be null,
            and ''predicate.test(null)'' will return false (which is the expected behaviour). */
            if (!predicate.test(value))
                return false;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder transitions = new StringBuilder("[");
        if (_initialState != null) {
            transitions.append(" (").append(_initialState).append(",").append(_initialState.getTransitions()).append(")");
        }
        for (IState state : _states.values()) {
            transitions.append(" (").append(state).append(",").append(state.getTransitions()).append(")");
        }
        if (_finalState != null) {
            transitions.append(" (").append(_finalState).append(",").append(_finalState.getTransitions()).append(")");
        }
        transitions.append(" ]");

        return transitions.toString();
    }
}
