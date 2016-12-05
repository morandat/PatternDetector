package fr.labri.patterndetector.runtime;

/**
 * Created by wbraik on 5/12/2016.
 */

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.ITransition;
import fr.labri.patterndetector.runtime.expressions.INacBeginMarker;
import fr.labri.patterndetector.runtime.expressions.INacEndMarker;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Deterministic, resetContext when no transition found
 */
public class DeterministicRunner extends AbstractAutomatonRunner implements Serializable {

    protected DeterministicRunContext _context;

    public DeterministicRunner(IRuleAutomaton automaton, int matchbufferSize) {
        super(automaton);

        _context = new DeterministicRunContext(automaton.getInitialState(), matchbufferSize);
    }

    public DeterministicRunner(IRuleAutomaton automaton, Matchbuffer matchbuffer) {
        super(automaton, true);

        _context = new DeterministicRunContext(automaton.getInitialState(), matchbuffer);
    }

    @Override
    public IRunContext getContext() {
        return _context;
    }

    @Override
    public void fire(Event e) {
        // Fire NACs
        ArrayList<DeterministicRunner> nacRunnersCopy = new ArrayList<>();
        nacRunnersCopy.addAll(_context.getNacRunners());
        nacRunnersCopy.forEach(nacRunner -> nacRunner.fire(e));

        ITransition t = _context.getCurrentState().pickTransition(e.getType());

        if (t == null) {
            Logger.debug(getContextId() + " : can't transition (" + e + ")");

            resetContext();
        } else {
            try {
                if (_context.isTransitionValid(t, e)) {
                    Logger.debug(getContextId() + " : transitioning : " + t + " (" + e + ")");

                    // Save current event in match buffer or discard it depending on the transition's type
                    switch (t.getType()) {
                        case TRANSITION_APPEND:
                            _context.appendEvent(e, t.getMatchbufferPosition());
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
                                    nacRunner.get().registerPatternObserver((Collection<Event> pattern) -> {
                                        Logger.debug(getContextId() + " : NAC matched, resetting run context");
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
                        Logger.debug(getContextId() + " : final state reached");

                        // If the final state has been reached, post the found pattern and resetContext the automaton
                        postPattern(_context.getMatchBuffer().asStream().collect(Collectors.toList()));
                        resetContext();
                    }
                }
            } catch (UnknownFieldException exception) {
                // TODO Do something
            }
        }
    }

    public void resetContext() {
        _context.updateCurrentState(_automaton.getInitialState());
        if (!_isNac) {
            _context.clearNacRunners();
            _context.clearMatchBuffers();
        }

        Logger.debug(getContextId() + " : run context reset");
    }

    @Override
    public long getContextId() {
        return _context.getContextId();
    }
}
