package fr.labri.streamchecking.automaton;

import fr.labri.streamchecking.EventType;
import fr.labri.streamchecking.automaton.IState;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by William Braik on 6/28/2015.
 */
public class State implements IState {

    protected StateType _type;
    protected String _label;
    protected Map<EventType, IState> _transitions;

    public State(StateType type, String label) {
        _type = type;
        _label = label;
        _transitions = new HashMap<>();
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
        if (next != null) {
            return next;
        } else {
            throw new Exception("NO TRANSITION FOR THIS EVENT"); //TODO StateException
        }
    }

    @Override
    public void registerTransition(EventType e, IState s) {
        _transitions.put(e, s);
    }

    @Override
    public boolean isFinal() {
        return _type == StateType.STATE_FINAL;
    }

    @Override
    public String toString() {
        return _label + "[" + _type + "]";
    }
}
