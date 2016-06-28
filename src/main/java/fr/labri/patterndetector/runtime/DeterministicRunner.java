package fr.labri.patterndetector.runtime;

/**
 * Created by wbraik on 5/12/2016.
 */

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.ITransition;
import fr.labri.patterndetector.rule.IRule;
import fr.labri.patterndetector.runtime.predicates.IStartNacMarker;
import fr.labri.patterndetector.runtime.predicates.IStopNacMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Deterministic, reset when no transition found
 */
public final class DeterministicRunner extends AbstractAutomatonRunner {

    private final Logger logger = LoggerFactory.getLogger(DeterministicRunner.class);

    private DeterministicRunContext _context;
    private Map<String, IRuleAutomaton> _nacAutomata; // maps NAC IDs to corresponding automata

    public DeterministicRunner(IRuleAutomaton automaton) {
        super(automaton);

        _context = new DeterministicRunContext(automaton.getInitialState());
        _nacAutomata = new HashMap<>();
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

                // Start NACs if there are any NAC START markers on the current transition
                if (!t.getStartNacMarkers().isEmpty()) {
                    for (IStartNacMarker startNacMarker : t.getStartNacMarkers()) {
                        startNac(startNacMarker.getNacId(), startNacMarker.getNacRule());
                    }
                }

                // Stop NACs if there are any NAC STOP markers on the current transition
                if (!t.getStopNacMarkers().isEmpty()) {
                    for (IStopNacMarker stopNacMarker : t.getStopNacMarkers()) {
                        stopNac(stopNacMarker.getNacId());
                    }
                }

                // Update the current state
                _context.updateCurrentState(t.getTarget());

                // Run function callbacks attached to the current state
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

        logger.debug("Automaton reset");
    }

    public void startNac(String nacId, IRule nacRule) {
        logger.debug("NAC started : " + nacId + " | " + nacRule);

        // TODO generate automaton and add it with key nacId
    }

    public void stopNac(String nacId) {
        logger.debug("NAC stopped : " + nacId);

        // TODO remove the automaton at key nacId
    }
}
