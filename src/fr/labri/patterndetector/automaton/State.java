package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.EventType;
import fr.labri.patterndetector.IEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by William Braik on 6/28/2015.
 */
public class State implements IState {

    protected boolean _isTake;
    protected String _label;
    protected Map<EventType, IState> _transitions;
    protected boolean _isFinal;

    public State(boolean isTake) {
        _isTake = isTake;
        _transitions = new HashMap<>();
        _isFinal = false;
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
        return _isFinal;
    }

    @Override
    public boolean isTake() {
        return _isTake;
    }

    @Override
    public void setLabel(String label) {
        _label = label;
    }

    @Override
    public void setFinal(boolean isFinal) {
        _isFinal = isFinal;
    }

    @Override
    public void setTake(boolean take) {
        _isTake = take;
    }

    @Override
    public void registerTransition(EventType e, IState s) {
        _transitions.put(e, s);
    }

    @Override
    public IState next(IEvent e) throws Exception {
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
            throw new Exception("Can't transition : " + e); //TODO StateException
        }
    }

    @Override
    public String toString() {
        return "STATE " + _label + (_isTake ? " [TAKE]" : " [IGNORE]");
    }
}
