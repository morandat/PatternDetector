package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.predicates.IPredicate;
import fr.labri.patterndetector.rule.visitors.RuleStringifier;
import fr.labri.patterndetector.runtime.predicates.INacBeginMarker;
import fr.labri.patterndetector.runtime.predicates.INacEndMarker;
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
    protected final Logger Logger = LoggerFactory.getLogger(getClass());

    protected int _matchbufferPosition;
    protected String _name;
    protected String _symbol;
    protected ArrayList<IPredicate> _predicates;
    protected ArrayList<INacBeginMarker> _startNacMarkers;
    protected ArrayList<INacEndMarker> _stopNacMarkers;
    protected Runnable _action;

    public AbstractRule(String symbol) {
        _symbol = symbol;
        _predicates = new ArrayList<>();
        _startNacMarkers = new ArrayList<>();
        _stopNacMarkers = new ArrayList<>();
        _action = null;
    }

    @Override
    public int getMatchbufferPosition() {
        return _matchbufferPosition;
    }

    @Override
    public IRule setMatchbufferPosition(int id) {
        _matchbufferPosition = id;

        return this;
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
    public ArrayList<INacBeginMarker> getNacBeginMarkers() {
        return _startNacMarkers;
    }

    @Override
    public ArrayList<INacEndMarker> getNacEndMarkers() {
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
    public IRule addNacBeginMarker(INacBeginMarker startNacMarker) {
        _startNacMarkers.add(startNacMarker);

        return this;
    }

    @Override
    public IRule setNacBeginMarkers(ArrayList<INacBeginMarker> startNacMarkers) {
        _startNacMarkers.addAll(startNacMarkers);

        return this;
    }

    @Override
    public IRule addNacEndMarker(INacEndMarker stopNacMarker) {
        _stopNacMarkers.add(stopNacMarker);

        return this;
    }

    @Override
    public IRule setNacEndMarkers(ArrayList<INacEndMarker> stopNacMarkers) {
        _stopNacMarkers.addAll(stopNacMarkers);

        return this;
    }

    @Override
    public String toString() {
        return RuleStringifier.stringify(this);
    }
}
