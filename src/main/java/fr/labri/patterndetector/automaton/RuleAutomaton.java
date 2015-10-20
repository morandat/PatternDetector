package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.IEvent;
import fr.labri.patterndetector.RuleManager;
import fr.labri.patterndetector.rules.IRule;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by William Braik on 6/28/2015.
 * <p>
 * A rule's automaton.
 * A type of timed automaton. Contains clocks for each event type.
 */

public class RuleAutomaton implements IRuleAutomaton {

    protected IRule _rule;
    protected IState _initialState;
    protected IState _finalState;
    protected Map<String, IState> _states;
    protected IState _currentState;
    protected ArrayList<IEvent> _matchBuffer;
    protected Map<String, Long> _clocks;

    public RuleAutomaton(IRule rule) {
        _rule = rule;
        _states = new HashMap<>();
        _matchBuffer = new ArrayList<>();
        _clocks = new HashMap<>();
    }

    @Override
    public IRule getRule() {
        return _rule;
    }

    @Override
    public String getRuleName() {
        return _rule.getName();
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
    public void registerInitialState(IState s) throws Exception {
        if (_initialState != null) {
            throw new Exception("An initial state has already been set !");
        }
        s.setLabel(State.LABEL_INITIAL);
        s.setInitial(true);
        s.setAutomaton(this);
        _initialState = s;
    }

    @Override
    public void registerState(IState s) {
        s.setLabel(Integer.toString(_states.size()));
        s.setInitial(false);
        s.setFinal(false);
        s.setAutomaton(this);
        _states.put(s.getLabel(), s);
    }

    @Override
    public void registerFinalState(IState s) throws Exception {
        if (_finalState != null) {
            throw new Exception("A final state has already been set !");
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

            // Update event clock
            _clocks.put(e.getType(), e.getTimestamp());

            ITransition t = _currentState.pickTransition(e);

            // If there is a transition, check its clock guards if any
            if (t != null
                    && checkClockGuard(e.getTimestamp(), t.getClockConstraint())
                    && testPredicates(e.getPayload(), t.getPredicates())) {
                System.out.println("Transitioning : " + t + " (" + e + ")");

                // Action to perform on the transition
                switch (t.getType()) {
                    case TRANSITION_APPEND:
                        _matchBuffer.add(e);
                        break;
                    case TRANSITION_DROP:
                }

                // Update current state
                _currentState = t.getTarget();

                if (_currentState.isFinal()) {
                    // If the final state has been reached, post the found pattern and reset the automaton
                    patternFound(_matchBuffer);
                    reset();
                    System.out.println("Final state reached");
                }
            } else {
                System.out.println("Can't transition ! (" + e + ")");
                reset();
            }

        } else {
            throw new Exception("Initial state not set !");
        }
    }

    @Override
    public void reset() {
        _currentState = _initialState;
        _matchBuffer.clear();
        _clocks.clear();
        System.out.println("Automaton reset");
    }

    @Override
    public void patternFound(Collection<IEvent> pattern) {
        RuleManager.getInstance().notifyPattern(pattern, _rule);
    }

    /**
     * Returns true if the clock guard is met, false otherwise
     */
    public boolean checkClockGuard(long currentTime, ClockGuard clockGuard) {
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

    public boolean testPredicates(Map<String, Integer> payload, Map<String, Predicate<Integer>> predicates) {
        if (payload == null || predicates == null) {
            return true;
        } else {
            for (Map.Entry<String, Predicate<Integer>> entry : predicates.entrySet()) {
                String field = entry.getKey();
                Predicate<Integer> predicate = entry.getValue();
                Integer value = payload.get(field);

                /* If the event misses a field that is required by a predicate, then value will be null,
                and predicate.test() will return false (which is the expected behaviour). */

                if (!predicate.test(value))
                    return false;
            }

            return true;
        }
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
