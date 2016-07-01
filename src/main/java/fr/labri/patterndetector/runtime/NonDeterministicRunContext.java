package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.automaton.IState;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by william.braik on 25/05/2016.
 * <p>
 * A non-deterministic context is essentially a collection of concurrent deterministic contexts
 */
public class NonDeterministicRunContext extends AbstractRunContext {

    private ArrayList<DeterministicRunContext> _subContexts;

    public NonDeterministicRunContext(IState initialState) {
        super();
        _subContexts = new ArrayList<>();
        DeterministicRunContext initialContext = new DeterministicRunContext(initialState);
        _subContexts.add(initialContext);
    }

    public ArrayList<DeterministicRunContext> getSubContexts() {
        return _subContexts;
    }

    public DeterministicRunContext addSubContext(IState initialState, Map<String, ArrayList<Event>> matchBuffers) {
        DeterministicRunContext subContext = new DeterministicRunContext(initialState, matchBuffers);
        _subContexts.add(subContext);

        return subContext;
    }

    public DeterministicRunContext addSubContext(IState initialState, Map<String, ArrayList<Event>> matchBuffers,
                                                 Map<String, DeterministicRunner> nacRunners) {
        DeterministicRunContext subContext = new DeterministicRunContext(initialState, matchBuffers, nacRunners);
        _subContexts.add(subContext);

        return subContext;
    }

    public void clearSubContexts() {
        _subContexts.clear();
    }
}
