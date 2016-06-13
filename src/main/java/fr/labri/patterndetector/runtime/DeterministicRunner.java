package fr.labri.patterndetector.runtime;

/**
 * Created by wbraik on 5/12/2016.
 */

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.ITransition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                logger.debug("Transitioning : " + t + " (" + e + ")");

                // Save current event in match buffer or discard it depending on the transition's type
                switch (t.getType()) {
                    case TRANSITION_APPEND:
                        _context.appendEvent(e, t.getMatchbufferKey());
                        break;

                    case TRANSITION_DROP:
                }

                // TODO Any NACs to start ?

                // update current state
                _context.updateCurrentState(t.getTarget());

                // function callbacks
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

    /**
     * Get back to initial state, clear match buffer
     */
    private void reset() {
        _context.updateCurrentState(_automaton.getInitialState());
        _context.clearMatchBuffers();
        // TODO _clocks.clear();

        logger.debug("Automaton reset");
    }
}
