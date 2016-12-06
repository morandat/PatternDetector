package fr.labri.patterndetector.runtime;

/**
 * Created by wbraik on 5/12/2016.
 * <p>
 * Fork and play
 */

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.automaton.ITransition;
import fr.labri.patterndetector.rule.INegationBeginMarker;
import fr.labri.patterndetector.rule.INegationEndMarker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class NonDeterministicRunner extends AbstractAutomatonRunner implements Serializable {

    private NonDeterministicRunContext _context;
    private RunnerMatchStrategy _matchStrategy;

    public NonDeterministicRunner(IRuleAutomaton automaton, RunnerMatchStrategy matchStrategy, int matchbufferSize) {
        super(automaton);
        _context = new NonDeterministicRunContext(automaton.getInitialState(), matchbufferSize);
        _matchStrategy = matchStrategy;
    }

    @Override
    public IRunContext getContext() {
        return _context;
    }

    @Override
    public void fire(Event e) {
        ArrayList<DeterministicRunContext> subContextsCopy = new ArrayList<>();
        subContextsCopy.addAll(_context.getSubContexts());

        // Find all the potentially matchable sub-contexts
        ArrayList<DeterministicRunContext> matchingSubContexts = findMatchingSubContexts(subContextsCopy, e);

        // Apply strategy to figure out which sub-contexts are to be actually matched
        List<DeterministicRunContext> subContextsToMatch = new ArrayList<>();
        switch (_matchStrategy) {
            case MatchAll:
                subContextsToMatch = matchingSubContexts;
                break;
            case MatchFirst:
                if (matchingSubContexts.size() > 1)
                    subContextsToMatch = matchingSubContexts.subList(0, 1);
                else
                    subContextsToMatch = matchingSubContexts;
                break;
            case MatchLast:
                if (matchingSubContexts.size() > 1)
                    subContextsToMatch = matchingSubContexts.subList(matchingSubContexts.size() - 1, matchingSubContexts.size());
                else
                    subContextsToMatch = matchingSubContexts;
                break;
            default:
        }

        matchSubContexts(subContextsToMatch, e);

        Logger.debug(_context.getContextId() + " : current contexts : " + _context.getSubContexts());
    }

    /**
     * Filter subContexts that match the current event
     *
     * @param subContexts All currently registered subContexts
     * @param e           Current event
     * @return subContexts that match e
     */
    private ArrayList<DeterministicRunContext> findMatchingSubContexts(ArrayList<DeterministicRunContext> subContexts, Event e) {
        ArrayList<DeterministicRunContext> matchingSubContexts = new ArrayList<>();

        for (DeterministicRunContext currentSubContext : subContexts) {
            // Fire negations
            ArrayList<DeterministicRunner> negationRunnersCopy = new ArrayList<>();
            negationRunnersCopy.addAll(currentSubContext.getNegationRunners());
            negationRunnersCopy.forEach(negationRunner -> negationRunner.fire(e));

            ITransition t = currentSubContext.getCurrentState().pickTransition(e.getType());

            if (t == null) {
                Logger.debug(currentSubContext.getContextId() + " : can't transition (" + e + ")");
            } else {
                try {
                    if (currentSubContext.isTransitionValid(t, e)) {
                        Logger.debug(currentSubContext.getContextId() + " : transitioning : " + t + " (" + e + ")");

                        // Save current event in match buffer or discard it depending on the transition's type
                        switch (t.getType()) {
                            case TRANSITION_APPEND:
                                matchingSubContexts.add(currentSubContext);
                                break;
                            case TRANSITION_DROP:
                        }
                    }
                } catch (UnknownFieldException exception) {
                    // TODO
                }
            }
        }
        return matchingSubContexts;
    }

    private void matchSubContexts(List<DeterministicRunContext> subContexts, Event e) {
        for (DeterministicRunContext currentSubContext : subContexts) {
            ITransition t = currentSubContext.getCurrentState().pickTransition(e.getType());

            // Update current state
            IState nextState = t.getTarget();

            if (!nextState.isFinal()) {
                DeterministicRunContext newSubContext = _context.addSubContext(nextState,
                        currentSubContext.getMatchBuffer(), currentSubContext.getNegationRunnersMap());
                Logger.debug(currentSubContext.getContextId() + " : new subcontext created (" + newSubContext.getContextId() + ")");

                // Update match buffer
                newSubContext.appendEvent(e, t.getMatchbufferPosition());

                // negation markers are ignored for negation runners
                if (!_isNegation) {
                    // Start negations if there are any negation begin markers on the current transition
                    if (!t.getNegationBeginMarkers().isEmpty()) {
                        for (INegationBeginMarker startNegationMarker : t.getNegationBeginMarkers()) {
                            Optional<DeterministicRunner> negationRunner = newSubContext.beginNegation(startNegationMarker.getNegationId(), startNegationMarker.getNegationRule());

                            if (negationRunner.isPresent()) {
                                negationRunner.get().registerPatternObserver((Collection<Event> pattern) -> {
                                    Logger.debug(newSubContext.getContextId() + " : negation matched, removing subcontext " + newSubContext.getContextId());
                                    _context.getSubContexts().remove(newSubContext);
                                });
                            }
                        }
                    }

                    // Stop negations if there are any negation end markers on the current transition
                    if (!t.getNegationEndMarkers().isEmpty()) {
                        for (INegationEndMarker negationEndMarker : t.getNegationEndMarkers()) {
                            newSubContext.endNegation(negationEndMarker.getNegationId());
                        }
                    }
                }
            } else {
                Logger.debug(currentSubContext.getContextId() + " : final state reached");

                ArrayList<Event> pattern = new ArrayList<>(currentSubContext.getMatchBuffer().asStream().collect(Collectors.toList()));
                pattern.add(e);
                postPattern(pattern);
            }

            // function callbacks
            nextState.performActions();
        }
    }

    @Override
    public void resetContext() { // FIXME currently unused
        _context.clearSubContexts();

        Logger.debug(_context.getContextId() + " run context reset");
    }

    @Override
    public long getContextId() {
        return _context.getContextId();
    }
}