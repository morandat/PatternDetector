package fr.labri.patterndetector.executor;

/**
 * Created by wbraik on 5/12/2016.
 */

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.automaton.ITransition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Deterministic, reset when no transition found
 */
public class DFARunner extends AbstractAutomatonRunner {

    private final Logger logger = LoggerFactory.getLogger(DFARunner.class);

    private IState _currentState;

    public DFARunner(IRuleAutomaton automaton) {
        super(automaton);

        _currentState = _currentStates.get(0);
    }

    @Override
    public void fire(IEvent e) {
        ITransition t = _currentState.pickTransition(e);

        if (t == null) {
            logger.debug("Can't transition (" + e + ")");

            reset();
        } else {
            if (testPredicates(t.getPredicates())) {
                // TODO check clock constraints

                logger.debug("Transitioning : " + t + " (" + e + ")");

                // Save current event in match buffer or discard it depending on the transition's type
                switch (t.getType()) {
                    case TRANSITION_APPEND:
                        _matchBuffer.add(e);
                        // Update event clock
                        // FIXME _clocks.put(e.getType(), e.getTimestamp());
                        break;
                    case TRANSITION_DROP:
                }

                // TODO Any NACs to start ?

                // Update current state
                _currentState = t.getTarget();

                // TODO exec callback function on state ?

                if (_currentState.isFinal()) {
                    logger.debug("Final state reached");

                    // If the final state has been reached, post the found pattern and reset the automaton
                    postPattern(_matchBuffer);
                    reset();
                }
            }
        }
    }

    /**
     * Get back to initial state, clear match buffer
     */
    private void reset() {
        _currentStates.clear();
        _currentStates.add(_automaton.getInitialState());
        _matchBuffer.clear();
        // FIXME _clocks.clear();

        logger.debug("Automaton reset");
    }

    @Override
    public ArrayList<IState> getCurrentStates() {
        ArrayList<IState> currentStates = new ArrayList<>();
        currentStates.add(_currentState);

        return currentStates;
    }
}
