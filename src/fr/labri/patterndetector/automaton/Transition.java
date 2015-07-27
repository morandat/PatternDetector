package fr.labri.patterndetector.automaton;

/**
 * Created by William Braik on 7/27/2015.
 */
public class Transition implements ITransition {

    private IState _source;
    private IState _target;
    private String _label;
    boolean _take;

    public Transition(IState source, IState target, String label, boolean take) {
        _source = source;
        _target = target;
        _label = label;
        _take = take;
    }

    @Override
    public IState getSource() {
        return _source;
    }

    @Override
    public IState getTarget() {
        return _target;
    }

    @Override
    public String getLabel() {
        return _label;
    }

    @Override
    public boolean isTake() {
        return _take;
    }

    @Override
    public String toString() {
        return _label + ": " + _source + " => " + _target;
    }
}
