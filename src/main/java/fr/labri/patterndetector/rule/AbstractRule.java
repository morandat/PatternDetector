package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.predicates.IPredicate;
import fr.labri.patterndetector.rule.visitors.RuleStringifier;
import fr.labri.patterndetector.runtime.predicates.IStartNacMarker;
import fr.labri.patterndetector.runtime.predicates.IStopNacMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by William Braik on 6/25/2015.
 * <p>
 * A pattern.
 */
public abstract class AbstractRule implements IRule {

    // Concrete subclasses should log with their own class names
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected String _name;
    protected String _symbol;
    protected ArrayList<IPredicate> _predicates;
    protected ArrayList<IStartNacMarker> _startNacMarkers;
    protected ArrayList<IStopNacMarker> _stopNacMarkers;
    protected Runnable _action;

    public AbstractRule(String symbol) {
        _symbol = symbol;
        _predicates = new ArrayList<>();
        _startNacMarkers = new ArrayList<>();
        _stopNacMarkers = new ArrayList<>();
        _action = null;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public IRule setName(String name) {
        _name = name;

        return this;
    }

    @Override
    public IRule setAction(Runnable action) {
        _action = action;

        return this;
    }

    @Override
    public String getSymbol() {
        return _symbol;
    }

    @Override
    public ArrayList<IPredicate> getPredicates() {
        return _predicates;
    }

    @Override
    public ArrayList<IStartNacMarker> getStartNacMarkers() {
        return _startNacMarkers;
    }

    @Override
    public ArrayList<IStopNacMarker> getStopNacMarkers() {
        return _stopNacMarkers;
    }

    @Override
    public Runnable getAction() {
        return _action;
    }

    @Override
    public IRule addPredicate(IPredicate predicate) {
        _predicates.add(predicate);

        return this;
    }

    @Override
    public IRule setPredicates(ArrayList<IPredicate> predicates) {
        _predicates.addAll(predicates);

        return this;
    }

    @Override
    public IRule addStartNacMarker(IStartNacMarker startNacMarker) {
        _startNacMarkers.add(startNacMarker);

        return this;
    }

    @Override
    public IRule setStartNacMarkers(ArrayList<IStartNacMarker> startNacMarkers) {
        _startNacMarkers.addAll(startNacMarkers);

        return this;
    }

    @Override
    public IRule addStopNacMarker(IStopNacMarker stopNacMarker) {
        _stopNacMarkers.add(stopNacMarker);

        return this;
    }

    @Override
    public IRule setStopNacMarkers(ArrayList<IStopNacMarker> stopNacMarkers) {
        _stopNacMarkers.addAll(stopNacMarkers);

        return this;
    }

    @Override
    public String toString() {
        return RuleStringifier.stringify(this);
    }
}
