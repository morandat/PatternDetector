package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.automaton.IState;

import java.util.ArrayList;

/**
 * Created by william.braik on 25/05/2016.
 * <p>
 * A non-deterministic context is essentially a collection of concurrent deterministic contexts
 */
public class NonDeterministicContext {

    private ArrayList<DeterministicRunContext> _contexts;

    public NonDeterministicContext(IState initialState) {
        _contexts = new ArrayList<>();
        DeterministicRunContext initialContext = new DeterministicRunContext(initialState);
        _contexts.add(initialContext);
    }


}
