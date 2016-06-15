package fr.labri.patterndetector.runtime;

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
 * Fork and play, no reset
 */
public final class NonDeterministicRunner extends AbstractAutomatonRunner {

    private final Logger logger = LoggerFactory.getLogger(NonDeterministicRunner.class);

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
                logger.debug("Can't transition (" + e + ")");
            } else {
                if (currentSubContext.testPredicates(t.getPredicates(), t.getMatchbufferKey(), e)) {
                    // TODO Any NACs to start ?

                    logger.debug("Transitioning : " + t + " (" + e + ")");

                    // Save current event in match buffer or discard it depending on the transition's type
                    switch (t.getType()) {
                        case TRANSITION_APPEND:
                            // Update current state
                            IState nextState = t.getTarget();

                            if (!nextState.isFinal()) {
                                DeterministicRunContext newSubContext = _context.addSubContext(nextState);

                                // Update match buffer
                                newSubContext.appendEvent(e, t.getMatchbufferKey());
                            }

                            // function callbacks
                            nextState.performActions();
                            break;

                        case TRANSITION_DROP:
                    }
                }
            }
        }

        logger.debug("Current states : " + _context.getSubContexts());
    }
}
