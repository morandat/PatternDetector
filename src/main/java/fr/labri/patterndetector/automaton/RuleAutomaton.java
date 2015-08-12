package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.IEvent;
import fr.labri.patterndetector.rules.IRule;

import java.util.*;

/**
 * Created by William Braik on 6/28/2015.
 */

/**
 * A type of timed automaton. Contains clocks for each event type
 */
public class RuleAutomaton implements IRuleAutomaton {

    protected IRule _rule;
    protected IState _initialState;
    protected IState _finalState;
    protected Map<String, IState> _states;
    protected IState _currentState;
    protected ArrayList<IEvent> _buffer;
    protected Map<String, Long> _clocks;

    public RuleAutomaton(IRule rule) {
        _rule = rule;
        _states = new HashMap<>();
        _buffer = new ArrayList<>();
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
    } // TODO if no final state, check if _rule is a Kleene, if yes return pivot state ?

    @Override
    public Collection<IEvent> getBuffer() {
        return _buffer;
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
        _states.put(s.getLabel(), s);
        s.setAutomaton(this);
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

            //System.out.println("Current state : " + _currentState);
            ITransition t = _currentState.pickTransition(e);

            // If there is a transition, check its clock guards if any
            if (t != null && checkClockGuard(e.getTimestamp(), t.getClockConstraint())) {
                System.out.println("Transitioning : " + t + " (" + e + ")");

                // Action to perform on the transition
                switch (t.getType()) {
                    case TRANSITION_APPEND:
                        _buffer.add(e);
                        break;
                    case TRANSITION_OVERWRITE:
                        _buffer.clear();
                        _buffer.add(e);
                        break;
                    case TRANSITION_DROP:
                }

                // Update current state
                _currentState = t.getTarget();

                if (_currentState.isFinal()) {
                    // If the final state has been reached, post the found pattern and reset the automaton
                    post(_buffer);
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
    public String toString() {
        StringBuilder transitions = new StringBuilder("[ ");
        if (_initialState != null) {
            transitions.append("(").append(_initialState).append(",").append(_initialState.getTransitions()).append(") ");
        }
        for (IState state : _states.values()) {
            transitions.append("(").append(state).append(",").append(state.getTransitions()).append(") ");
        }
        if (_finalState != null) {
            transitions.append("(").append(_finalState).append(",").append(_finalState.getTransitions()).append(")");
        }
        transitions.append(" ]");

        return transitions.toString();
    }

    @Override
    public void reset() {
        _currentState = _initialState;
        _buffer.clear();
        _clocks.clear();
        System.out.println("Automaton reset");
    }

    public void post(Collection<IEvent> pattern) {
        System.out.println("*** PATTERN FOUND : " + pattern + " ***");
        // TODO post() should be in RuleManager (in RuleAutomaton : _rule.post(); in Rule : _ruleManager.post();)
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
}
