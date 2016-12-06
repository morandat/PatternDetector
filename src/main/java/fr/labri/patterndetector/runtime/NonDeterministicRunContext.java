package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.automaton.IState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by william.braik on 25/05/2016.
 * <p>
 * A non-deterministic context is essentially a collection of concurrent deterministic contexts
 */
public class NonDeterministicRunContext extends AbstractRunContext implements Serializable {

    private ArrayList<DeterministicRunContext> _subContexts;

    public NonDeterministicRunContext(IState initialState, int matchbufferSize) {
        super();
        _subContexts = new ArrayList<>();
        DeterministicRunContext initialContext = new DeterministicRunContext(initialState, matchbufferSize);
        _subContexts.add(initialContext);
    }

    public ArrayList<DeterministicRunContext> getSubContexts() {
        return _subContexts;
    }

    public DeterministicRunContext addSubContext(IState initialState, Matchbuffer matchBuffer) {
        DeterministicRunContext subContext = new DeterministicRunContext(initialState, matchBuffer);
        _subContexts.add(subContext);

        return subContext;
    }

    public DeterministicRunContext addSubContext(IState initialState, Matchbuffer matchBuffer,
                                                 Map<String, DeterministicRunner> negationRunners) {
        DeterministicRunContext subContext = new DeterministicRunContext(initialState, matchBuffer, negationRunners);
        _subContexts.add(subContext);

        return subContext;
    }

    public void clearSubContexts() {
        _subContexts.clear();
    }

    @Override
    public String toString() {
        return getSubContexts().toString();
    }
}
