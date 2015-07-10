package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.EventType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by William Braik on 6/28/2015.
 */
public class State implements IState {

    protected StateType _type;
    protected String _label;
    protected Map<EventType, IState> _transitions;
    protected boolean _isFinal;

    public State(StateType type, String label) {
        _type = type;
        _label = label;
        _transitions = new HashMap<>();
        _isFinal = false;
    }

    @Override
    public StateType getType() {
        return _type;
    }

    @Override
    public String getLabel() {
        return _label;
    }

    @Override
    public IState next(EventType e) throws Exception {
        IState next = _transitions.get(e);

        // If the event can't fire any transition from this state, check if the state has a negative transition.
        // The negative transition HAS to be checked last.
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
    public void registerTransition(EventType e, IState s) {
        _transitions.put(e, s);
    }

    @Override
    public void setFinal(boolean isFinal) {
        _isFinal = isFinal;
    }

    @Override
    public boolean isFinal() {
        return _isFinal;
    }

    @Override
    public String toString() {
        return _label + "[" + _type + "]";
    }
}
