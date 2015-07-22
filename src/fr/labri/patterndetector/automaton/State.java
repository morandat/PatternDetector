package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.EventType;
import fr.labri.patterndetector.IEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by William Braik on 6/28/2015.
 */
public class State implements IState {

    protected boolean _take;
    protected String _label;
    protected Map<EventType, IState> _transitions;
    protected boolean _final;

    public State(boolean take) {
        _take = take;
        _transitions = new HashMap<>();
        _final = false;
    }

    @Override
    public String getLabel() {
        return _label;
    }

    @Override
    public Map<EventType, IState> getTransitions() {
        return _transitions;
    }

    @Override
    public boolean isFinal() {
        return _final;
    }

    @Override
    public boolean isTake() {
        return _take;
    }

    @Override
    public void setLabel(String label) {
        _label = label;
    }

    @Override
    public void setFinal(boolean isFinal) {
        _final = isFinal;
    }

    @Override
    public void setTake(boolean take) {
        _take = take;
    }

    @Override
    public void registerTransition(EventType e, IState s) {
        _transitions.put(e, s);
    }

    @Override
    public IState next(IEvent e) {
        IState next = _transitions.get(e.getType());

        // If the event can't fire any transition from this state, check if the state has a negative transition.
        // The negative transition must to be checked last.
        if (next == null) {
            next = _transitions.get(EventType.EVENT_NEGATION);
        }

        if (next != null) {
            System.out.println("Transitioning : " + e);
            return next;
        } else {
            System.out.println("Can't transition : " + e);
            return this;
        }
    }

    @Override
    public String toString() {
        return "STATE " + _label + (_take ? " [TAKE]" : " [IGNORE]");
    }
}
