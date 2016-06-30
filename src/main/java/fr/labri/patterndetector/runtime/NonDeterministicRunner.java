package fr.labri.patterndetector.runtime;

/**
 * Created by wbraik on 5/12/2016.
 */

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.automaton.ITransition;

import java.util.ArrayList;

/**
 * Fork and play, no reset
 */
public final class NonDeterministicRunner extends AbstractAutomatonRunner {

    private NonDeterministicRunContext _context;

    public NonDeterministicRunner(IRuleAutomaton automaton) {
        super(automaton);

        _context = new NonDeterministicRunContext(automaton.getInitialState());
    }

    @Override
    public void fire(IEvent e) {
        ArrayList<DeterministicRunContext> subContextsCopy = new ArrayList<>();
        subContextsCopy.addAll(_context.getSubContexts());

        for (DeterministicRunContext currentSubContext : subContextsCopy) {
            IState s = currentSubContext.getCurrentState();
            ITransition t = s.pickTransition(e);

            if (t == null) {
                Logger.debug("Can't transition (" + e + ")");
            } else {
                if (currentSubContext.testPredicates(t.getPredicates(), t.getMatchbufferKey(), e)) {
                    Logger.debug("Transitioning : " + t + " (" + e + ")");

                    // Save current event in match buffer or discard it depending on the transition's type
                    switch (t.getType()) {
                        case TRANSITION_APPEND:
                            // Update current state
                            IState nextState = t.getTarget();

                            if (!nextState.isFinal()) {
                                DeterministicRunContext newSubContext = _context.addSubContext(nextState);

                                // Update match buffer
                                newSubContext.appendEvent(e, t.getMatchbufferKey()); // FIXME non-deterministic matchbuffers are buggy. All subcontexts should share the parent matchbuffer
                            } else {
                                Logger.debug("Final state reached");

                                // If the final state has been reached, post the found pattern and resetContext the automaton
                                //postPattern(_context.getMatchBuffers().collect(Collectors.toList())); FIXME buggy
                            }

                            // function callbacks
                            nextState.performActions();
                            break;

                        case TRANSITION_DROP:
                    }
                }
            }
        }

        Logger.debug("Current states : " + _context.getSubContexts());
    }
}
