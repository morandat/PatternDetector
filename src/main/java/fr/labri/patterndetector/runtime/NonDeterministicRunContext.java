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
public class NonDeterministicRunContext {

    private ArrayList<DeterministicRunContext> _subContexts;

    public NonDeterministicRunContext(IState initialState) {
        _subContexts = new ArrayList<>();
        DeterministicRunContext initialContext = new DeterministicRunContext(initialState);
        _subContexts.add(initialContext);
    }

    public ArrayList<DeterministicRunContext> getSubContexts() {
        return _subContexts;
    }

    public DeterministicRunContext addSubContext(IState initialState) {
        DeterministicRunContext subContext = new DeterministicRunContext(initialState);
        _subContexts.add(subContext);

        return subContext;
    }

    public DeterministicRunContext addSubContext(IState initialState, Map<String, ArrayList<IEvent>> matchBuffers) {
        DeterministicRunContext subContext = new DeterministicRunContext(initialState, matchBuffers);
        _subContexts.add(subContext);

        return subContext;
    }
}
