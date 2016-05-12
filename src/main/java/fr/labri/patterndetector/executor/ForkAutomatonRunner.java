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
 * Fork and play, no reset
 */
public class ForkAutomatonRunner extends AbstractAutomatonRunner {

    private final Logger logger = LoggerFactory.getLogger(ForkAutomatonRunner.class);

    public ForkAutomatonRunner(IRuleAutomaton automaton) {
        super(automaton);
    }

    @Override
    public void fire(IEvent e) {
        ArrayList<IState> currentStatesCopy = new ArrayList<>();
        currentStatesCopy.addAll(_currentStates);

        for (IState s : currentStatesCopy) {
            ITransition t = s.pickTransition(e);

            if (t == null) {
                logger.debug("Can't transition (" + e + ")");
            } else {
                // TODO check clock constraints and predicates globally

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

                // TODO exec callback function on state ?

                // Update current state
                IState nextState = t.getTarget();
                if (!nextState.isFinal()) {
                    _currentStates.add(nextState);
                }
            }
        }
    }
}