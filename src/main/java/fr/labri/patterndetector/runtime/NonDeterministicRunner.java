package fr.labri.patterndetector.runtime;

/**
 * Created by wbraik on 5/12/2016.
 */

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.automaton.ITransition;
import fr.labri.patterndetector.runtime.predicates.INacBeginMarker;
import fr.labri.patterndetector.runtime.predicates.INacEndMarker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public void fire(Event e) {
        ArrayList<DeterministicRunContext> subContextsCopy = new ArrayList<>();
        subContextsCopy.addAll(_context.getSubContexts());

        for (DeterministicRunContext currentSubContext : subContextsCopy) {
            ITransition t = currentSubContext.getCurrentState().pickTransition(e);

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
                                DeterministicRunContext newSubContext = _context.addSubContext(nextState, currentSubContext.getMatchBuffersMap());

                                // Update match buffer
                                newSubContext.appendEvent(e, t.getMatchbufferKey());
                            } else {
                                Logger.debug("Final state reached");

                                ArrayList<Event> pattern = new ArrayList<>(currentSubContext.getMatchBuffersStream().collect(Collectors.toList()));
                                pattern.add(e);
                                postPattern(pattern);
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

    @Override
    public void resetContext() {
        _context.clearSubContexts();

        Logger.debug("Run context reset");
    }
}
