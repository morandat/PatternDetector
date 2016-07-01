package fr.labri.patterndetector.runtime;

/**
 * Created by wbraik on 5/12/2016.
 */

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.ITransition;
import fr.labri.patterndetector.runtime.predicates.INacBeginMarker;
import fr.labri.patterndetector.runtime.predicates.INacEndMarker;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Deterministic, resetContext when no transition found
 */
public class DeterministicRunner extends AbstractAutomatonRunner {

    protected DeterministicRunContext _context;

    public DeterministicRunner(IRuleAutomaton automaton) {
        super(automaton);

        _context = new DeterministicRunContext(automaton.getInitialState());
    }

    public DeterministicRunner(IRuleAutomaton automaton, Map<String, ArrayList<IEvent>> matchBuffers) {
        super(automaton, true);

        _context = new DeterministicRunContext(automaton.getInitialState(), matchBuffers);
    }

    @Override
    public void fire(IEvent e) {
        ArrayList<DeterministicRunner> nacRunnersCopy = new ArrayList<>();
        nacRunnersCopy.addAll(_context.getNacRunners());
        nacRunnersCopy.forEach(nacRunner -> nacRunner.fire(e));

        ITransition t = _context.getCurrentState().pickTransition(e);

        if (t == null) {
            Logger.debug("Can't transition (" + e + ")");

            resetContext();
        } else {
            if (_context.testPredicates(t.getPredicates(), t.getMatchbufferKey(), e)) {
                Logger.debug("Transitioning : " + t + " (" + e + ")");

                // Save current event in match buffer or discard it depending on the transition's type
                switch (t.getType()) {
                    case TRANSITION_APPEND:
                        _context.appendEvent(e, t.getMatchbufferKey());
                        break;

                    case TRANSITION_DROP:
                }

                // NAC markers are ignored for NAC runners
                if (!_isNac) {
                    // Start NACs if there are any NAC START markers on the current transition
                    if (!t.getNacBeginMarkers().isEmpty()) {
                        for (INacBeginMarker startNacMarker : t.getNacBeginMarkers()) {
                            Optional<DeterministicRunner> nacRunner = _context.startNac(startNacMarker.getNacId(), startNacMarker.getNacRule());

                            if (nacRunner.isPresent()) {
                                nacRunner.get().registerPatternObserver((Collection<IEvent> pattern) -> {
                                    Logger.debug("NAC matched, resetting run context");
                                    resetContext();
                                });
                            }
                        }
                    }

                    // Stop NACs if there are any NAC STOP markers on the current transition
                    if (!t.getNacEndMarkers().isEmpty()) {
                        for (INacEndMarker stopNacMarker : t.getNacEndMarkers()) {
                            _context.stopNac(stopNacMarker.getNacId());
                        }
                    }
                }

                // Update the current state
                _context.updateCurrentState(t.getTarget());

                // Run function callbacks attached to the current state
                _context.getCurrentState().performActions();

                if (_context.isCurrentStateFinal()) {
                    Logger.debug("Final state reached");

                    // If the final state has been reached, post the found pattern and resetContext the automaton
                    postPattern(_context.getMatchBuffersAsStream().collect(Collectors.toList()));
                    resetContext();
                }
            }
        }
    }

    private void resetContext() {
        _context.updateCurrentState(_automaton.getInitialState());
        if (!_isNac) {
            _context.clearNacRunners();
            _context.clearMatchBuffers();
        }

        Logger.debug("Run context reset");
    }
}
