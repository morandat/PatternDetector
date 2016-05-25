package fr.labri.patterndetector.executor;

/**
 * Created by wbraik on 5/12/2016.
 */

import fr.labri.patterndetector.automaton.ClockGuard;
import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.automaton.ITransition;
import fr.labri.patterndetector.executor.predicates.IPredicate;
import fr.labri.patterndetector.types.IValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Deterministic, reset when no transition found
 */
public final class DeterministicRunner extends AbstractAutomatonRunner {

    private final Logger logger = LoggerFactory.getLogger(DeterministicRunner.class);

    private DeterministicRunContext _context;

    public DeterministicRunner(IRuleAutomaton automaton) {
        super(automaton);

        _context = new DeterministicRunContext(automaton.getInitialState());
    }

    @Override
    public void fire(IEvent e) {
        ITransition t = _context.getCurrentState().pickTransition(e);

        if (t == null) {
            logger.debug("Can't transition (" + e + ")");

            reset();
        } else {
            if (_context.testPredicates(t.getPredicates(), t.getMatchbufferKey(), e)) {
                // TODO check clock constraints

                logger.debug("Transitioning : " + t + " (" + e + ")");

                // Save current event in match buffer or discard it depending on the transition's type
                switch (t.getType()) {
                    case TRANSITION_APPEND:
                        // update matchbuffer
                        _context.appendEvent(e, t.getMatchbufferKey());

                        // update event clock
                        // FIXME _clocks.put(e.getType(), e.getTimestamp());
                        break;

                    case TRANSITION_DROP:
                }

                // TODO Any NACs to start ?

                // update current state
                _context.updateState(t.getTarget());

                // activate function callbacks
                _context.getCurrentState().performActions();

                if (_context.isCurrentStateFinal()) {
                    logger.debug("Final state reached");

                    // If the final state has been reached, post the found pattern and reset the automaton
                    postPattern(_context.getMatchBuffers().collect(Collectors.toList()));
                    reset();
                }
            }
        }
    }

    public DeterministicRunContext getContext() {
        return _context;
    }

    /**
     * Get back to initial state, clear match buffer
     */
    private void reset() {
        _context.updateState(_automaton.getInitialState());
        _context.clearMatchBuffers();
        // TODO _clocks.clear();

        logger.debug("Automaton reset");
    }
}
