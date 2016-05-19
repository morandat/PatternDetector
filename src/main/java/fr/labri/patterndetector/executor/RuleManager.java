package fr.labri.patterndetector.executor;
/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Entity which maintains a rule set and dispatches events to rule automata.
 */

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.IState;
import fr.labri.patterndetector.automaton.exception.*;
import fr.labri.patterndetector.rule.visitors.RuleAutomatonMaker;
import fr.labri.patterndetector.rule.IRule;
import fr.labri.patterndetector.rule.visitors.RuleNamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class RuleManager implements IPatternObserver {

    private final Logger logger = LoggerFactory.getLogger(RuleManager.class);

    private ArrayList<IAutomatonRunner> _runners; // Maps rule names to rule automaton runners
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
        RuleNamer.nameRules(rule);
        IRuleAutomaton automaton = RuleAutomatonMaker.makeAutomaton(rule); // Try to build the rule automaton.

        IRuleAutomaton powerset = automaton.powerset();

        try {
            powerset.validate();
        } catch (NoFinalStateException e) {
            logger.error("Non-terminating rule : " + rule + " (" + e.getMessage() + ")\n" + e.getRuleAutomaton());

            throw new RuntimeException("Non-terminating rule : " + rule + " (" + e.getMessage() + ")\n"
                    + e.getRuleAutomaton());
        } catch (UnreachableStatesException e) {
            logger.error("Ambiguous rule : " + rule + " (" + e.getMessage() + ")\n" + e.getRuleAutomaton());

            throw new RuntimeException("Ambiguous rule : " + rule + " (" + e.getMessage() + ")\n"
                    + e.getRuleAutomaton());
        } catch (RuleAutomatonException e) {
            logger.error("Invalid rule : " + rule + " (" + e.getMessage() + ")\n" + e.getRuleAutomaton());

            throw new RuntimeException("Invalid rule : " + rule + " (" + e.getMessage() + ")\n" + e.getRuleAutomaton());
        }

        // Instantiate runner
        try {
            IAutomatonRunner runner = _runnerFactory.getRunner(runnerType, powerset);
            runner.registerPatternObserver(this);
            _runners.add(runner);

            logger.info("Rule added : " + rule);
            logger.debug("Powerset : " + powerset);

            return runner;
        } catch (Exception e) {
            throw new RuntimeException("Could not instantiate runner class : " + e.getClass());
        }
    }

    public void dispatchEvent(IEvent event) {
        _runners.forEach(runner -> runner.fire(event));
    }

    @Override
    public void notifyPattern(Collection<IEvent> pattern) {
        logger.info("Pattern found : " + pattern);
    }
}
