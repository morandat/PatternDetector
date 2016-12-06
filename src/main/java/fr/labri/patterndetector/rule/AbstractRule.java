package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.expressions.IPredicate;
import fr.labri.patterndetector.rule.visitors.RuleStringifier;
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
    protected ArrayList<INegationBeginMarker> _negationStartMarkers;
    protected ArrayList<INegationEndMarker> _negationStopMarkers;
    protected Runnable _action; // TODO remove

    public AbstractRule(String symbol) {
        _symbol = symbol;
        _predicates = new ArrayList<>();
        _negationStartMarkers = new ArrayList<>();
        _negationStopMarkers = new ArrayList<>();
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
    public ArrayList<INegationBeginMarker> getNegationBeginMarkers() {
        return _negationStartMarkers;
    }

    @Override
    public ArrayList<INegationEndMarker> getNegationEndMarkers() {
        return _negationStopMarkers;
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
    public IRule addNegationBeginMarker(INegationBeginMarker negationBeginMarker) {
        _negationStartMarkers.add(negationBeginMarker);

        return this;
    }

    @Override
    public IRule setNegationBeginMarkers(ArrayList<INegationBeginMarker> negationBeginMarkers) {
        _negationStartMarkers.addAll(negationBeginMarkers);

        return this;
    }

    @Override
    public IRule addNegationEndMarker(INegationEndMarker negationEndMarker) {
        _negationStopMarkers.add(negationEndMarker);

        return this;
    }

    @Override
    public IRule setNegationEndMarkers(ArrayList<INegationEndMarker> negationEndMarkers) {
        _negationStopMarkers.addAll(negationEndMarkers);

        return this;
    }

    @Override
    public String toString() {
        return RuleStringifier.stringify(this);
    }
}
