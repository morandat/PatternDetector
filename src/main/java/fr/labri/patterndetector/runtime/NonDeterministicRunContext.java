package fr.labri.patterndetector.runtime;

import fr.labri.patterndetector.automaton.IState;

import java.util.ArrayList;
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

    public Stream<IEvent> getMatchBuffers() {
        ArrayList<IEvent> matchBuffer = new ArrayList<>();

        for (DeterministicRunContext subContext : _subContexts) {
            subContext.getMatchBuffers().forEach(matchBuffer::add);
        }

        return matchBuffer.stream()
                .sorted((e1, e2) -> new Long(e1.getTimestamp()).compareTo(e2.getTimestamp())); // make sure it's sorted by timestamp
    }
}
