package fr.labri.patterndetector.runtime;

/**
 * Created by wbraik on 5/12/2016.
 * <p>
 * Fork and play
 */

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.automaton.ITransition;
import fr.labri.patterndetector.runtime.predicates.INacBeginMarker;
import fr.labri.patterndetector.runtime.predicates.INacEndMarker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class NonDeterministicRunner extends AbstractAutomatonRunner implements Serializable {

    private NonDeterministicRunContext _context;
    private RunnerMatchStrategy _matchStrategy;

    public NonDeterministicRunner(IRuleAutomaton automaton, RunnerMatchStrategy matchStrategy) {
        super(automaton);
        _context = new NonDeterministicRunContext(automaton.getInitialState());
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
            // Fire NACs
            ArrayList<DeterministicRunner> nacRunnersCopy = new ArrayList<>();
            nacRunnersCopy.addAll(currentSubContext.getNacRunners());
            nacRunnersCopy.forEach(nacRunner -> nacRunner.fire(e));

            ITransition t = currentSubContext.getCurrentState().pickTransition(e.getType());

            if (t == null) {
                Logger.debug(currentSubContext.getContextId() + " : can't transition (" + e + ")");
            } else {
                if (currentSubContext.testPredicates(t.getPredicates(), t.getMatchbufferKey(), e)) {
                    Logger.debug(currentSubContext.getContextId() + " : transitioning : " + t + " (" + e + ")");

                    // Save current event in match buffer or discard it depending on the transition's type
                    switch (t.getType()) {
                        case TRANSITION_APPEND:
                            matchingSubContexts.add(currentSubContext);
                            break;
                        case TRANSITION_DROP:
                    }
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
                        currentSubContext.getMatchBuffersMap(), currentSubContext.getNacRunnersMap());
                Logger.debug(currentSubContext.getContextId() + " : new subcontext created (" + newSubContext.getContextId() + ")");

                // Update match buffer
                newSubContext.appendEvent(e, t.getMatchbufferKey());

                // NAC markers are ignored for NAC runners
                if (!_isNac) {
                    // Start NACs if there are any NAC START markers on the current transition
                    if (!t.getNacBeginMarkers().isEmpty()) {
                        for (INacBeginMarker startNacMarker : t.getNacBeginMarkers()) {
                            Optional<DeterministicRunner> nacRunner = newSubContext.startNac(startNacMarker.getNacId(), startNacMarker.getNacRule());

                            if (nacRunner.isPresent()) {
                                nacRunner.get().registerPatternObserver((Collection<Event> pattern) -> {
                                    Logger.debug(newSubContext.getContextId() + " : NAC matched, removing subcontext " + newSubContext.getContextId());
                                    _context.getSubContexts().remove(newSubContext);
                                });
                            }
                        }
                    }

                    // Stop NACs if there are any NAC STOP markers on the current transition
                    if (!t.getNacEndMarkers().isEmpty()) {
                        for (INacEndMarker stopNacMarker : t.getNacEndMarkers()) {
                            newSubContext.stopNac(stopNacMarker.getNacId());
                        }
                    }
                }
            } else {
                Logger.debug(currentSubContext.getContextId() + " : final state reached");

                ArrayList<Event> pattern = new ArrayList<>(currentSubContext.getMatchBuffersStream().collect(Collectors.toList()));
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
