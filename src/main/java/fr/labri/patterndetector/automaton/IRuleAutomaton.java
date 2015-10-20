package fr.labri.patterndetector.automaton;

import fr.labri.patterndetector.IEvent;
import fr.labri.patterndetector.rules.IRule;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

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

    void registerInitialState(IState s) throws Exception;  //TODO AutomatonException;

    void registerState(IState s);

    void registerFinalState(IState s) throws Exception; //TODO AutomatonException;

    void fire(IEvent e) throws Exception; //TODO AutomatonException

    void reset();

    /**
     * Action performed when a pattern is found (i.e. when the final state is reached)
     *
     * @param pattern
     */
    void patternFound(Collection<IEvent> pattern);
}
