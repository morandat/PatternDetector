package fr.labri.patterndetector.runtime;

/**
 * Created by wbraik on 5/12/2016.
 */

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.ITransition;
import fr.labri.patterndetector.rule.INegationBeginMarker;
import fr.labri.patterndetector.rule.INegationEndMarker;

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
        // Fire negations
        ArrayList<DeterministicRunner> negationRunnersCopy = new ArrayList<>();
        negationRunnersCopy.addAll(_context.getNegationRunners());
        negationRunnersCopy.forEach(negationRunner -> negationRunner.fire(e));

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

                    // ngeation markers are ignored for negation runners
                    if (!_isNegation) {
                        // Start negations if there are any negation begin markers on the current transition
                        if (!t.getNegationBeginMarkers().isEmpty()) {
                            for (INegationBeginMarker negationBeginMarker : t.getNegationBeginMarkers()) {
                                Optional<DeterministicRunner> negationRunner = _context.beginNegation(negationBeginMarker.getNegationId(), negationBeginMarker.getNegationRule());

                                if (negationRunner.isPresent()) {
                                    negationRunner.get().registerPatternObserver((Collection<Event> pattern) -> {
                                        Logger.debug(getContextId() + " : negation matched, resetting run context");
                                        resetContext();
                                    });
                                }
                            }
                        }
                        // Stop negations if there are any negation end markers on the current transition
                        if (!t.getNegationEndMarkers().isEmpty()) {
                            for (INegationEndMarker negationEndMarker : t.getNegationEndMarkers()) {
                                _context.endNegation(negationEndMarker.getNegationId());
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
        if (!_isNegation) {
            _context.clearNegationRunners();
            _context.clearMatchBuffers();
        }

        Logger.debug(getContextId() + " : run context reset");
    }

    @Override
    public long getContextId() {
        return _context.getContextId();
    }
}
