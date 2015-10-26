package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.automaton.exception.AutomatonException;
import fr.labri.patterndetector.executor.IEvent;
import fr.labri.patterndetector.executor.IPatternObserver;
import fr.labri.patterndetector.rules.IRule;

import java.util.Collection;
import java.util.Map;

/**
 * Created by William Braik on 6/28/2015.
 * <p>
 * A rule's automaton.
 * A type of timed automaton. Contains clocks for each event type.
 */
public interface IRuleAutomaton {

    IRule getRule();

    IState getCurrentState();

    IState getInitialState();

    IState getStateByLabel(String label);

    Map<String, IState> getStates();

    IState getFinalState();

    Collection<IEvent> getMatchBuffer();

    Collection<ITransition> getTransitions();

    void setInitialState(IState s) throws AutomatonException;

    void addState(IState s);

    void setFinalState(IState s) throws AutomatonException;

    void fire(IEvent e) throws Exception; //TODO AutomatonException

    void reset();

    /**
     * Register a pattern observer.
     *
     * @param observer The pattern observer to register.
     */
    void registerPatternObserver(IPatternObserver observer);

    /**
     * Action performed when a pattern is found (i.e. when the final state is reached)
     *
     * @param pattern The detected pattern.
     */
    void patternDetected(Collection<IEvent> pattern);

    /**
     * @return The corresponding powerset automaton of this automaton.
     */
    default IRuleAutomaton powerset() {
        return AutomatonUtils.powerset(this);
    }

    /**
     * @return A copy of this automaton.
     */
    default IRuleAutomaton copy() {
        return AutomatonUtils.copy(this);
    }
}
