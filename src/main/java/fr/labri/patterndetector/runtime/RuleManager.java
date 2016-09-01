package fr.labri.patterndetector.runtime;
/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Entity which maintains a rule set and dispatches events to rule automata.
 */

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.rule.visitors.RuleAutomatonMaker;
import fr.labri.patterndetector.rule.IRule;
import fr.labri.patterndetector.rule.visitors.RulesNamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public final class RuleManager implements IPatternObserver {

    private final Logger Logger = LoggerFactory.getLogger(RuleManager.class);

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
        RulesNamer.nameRules(rule);
        IRuleAutomaton automaton = RuleAutomatonMaker.makeAutomaton(rule); // Try to build the rule automaton.

        IRuleAutomaton powerset = automaton.powerset();

        powerset.validate();

        // Instantiate runner
        try {
            IAutomatonRunner runner = _runnerFactory.getRunner(runnerType, powerset);
            runner.registerPatternObserver(this);
            _runners.add(runner);

            Logger.info("Rule added : " + rule);
            Logger.debug("Powerset : " + powerset);

            serializeAutomaton(powerset, "D:\\automaton_" + rule.getName() + ".ser");

            return runner;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not add rule : " + e.getMessage());
        }
    }

    public void dispatchEvent(Event event) {
        _runners.forEach(runner -> runner.fire(event));
    }

    @Override
    public void notifyPattern(Collection<Event> pattern) {
        Logger.info("Pattern found : " + pattern);
    }

    private void serializeAutomaton(IRuleAutomaton automaton, String file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(automaton);
        Logger.debug("Automaton serialized");
    }
}
