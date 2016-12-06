package fr.labri.patterndetector.runtime;
/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Entity which maintains a rule set and dispatches events to rule automata.
 */

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.rule.visitors.RuleAutomatonMaker;
import fr.labri.patterndetector.rule.IRule;
import fr.labri.patterndetector.rule.visitors.RulesNumberer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class RuleManager implements IPatternObserver {

    private final Logger Logger = LoggerFactory.getLogger(RuleManager.class);

    private ArrayList<IAutomatonRunner> _runners;
    private AutomatonRunnerFactory _runnerFactory;

    public RuleManager() {
        _runners = new ArrayList<>();
        _runnerFactory = new AutomatonRunnerFactory();
    }

    /**
     * Validate a rule and if it is valid, add it to the rule set.
     *
     * @param rule The rule to add.
     * @return The rule's name.
     */
    public IAutomatonRunner addRule(IRule rule, AutomatonRunnerType runnerType) {
        int matchbufferSize = RulesNumberer.numberRule(rule);
        IRuleAutomaton automaton = RuleAutomatonMaker.makeAutomaton(rule);
        IRuleAutomaton powerset = automaton.powerset();
        powerset.validate();

        // Instantiate automaton runner
        IAutomatonRunner runner = _runnerFactory.getRunner(runnerType, powerset, matchbufferSize);
        runner.registerPatternObserver(this);
        _runners.add(runner);

        Logger.info("Rule added : " + rule);
        Logger.debug("Powerset : " + powerset);

        return runner;
    }

    public void dispatchEvent(Event event) {
        _runners.forEach(runner -> runner.fire(event));
    }

    @Override
    public void notifyPattern(Collection<Event> pattern) {
        Logger.info("Pattern found : " + pattern);
    }
}
