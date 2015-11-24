package fr.labri.patterndetector.executor;
/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Entity which maintains a rule set and dispatches events to rule automata.
 */

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.automaton.exception.*;
import fr.labri.patterndetector.compiler.RuleCompiler;
import fr.labri.patterndetector.rules.IRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class RuleManager implements IPatternObserver {

    private final Logger logger = LoggerFactory.getLogger(RuleManager.class);
    private int _ruleCounter; // Internal rule counter, used to build rule IDs
    private Map<String, IRule> _rules; // Maps rule names to the rules themselves
    private Map<String, IRuleAutomaton> _automata; // Maps rule names to rule automata
    private Collection<IEvent> _lastPattern; // Last detected pattern

    public RuleManager() {
        _ruleCounter = 0;
        _rules = new HashMap<>();
        _automata = new HashMap<>();
        _lastPattern = new ArrayList<>();
    }

    /**
     * Validate a rule and if it is valid, add it to the rule set.
     *
     * @param rule The rule to add.
     * @return The rule's name.
     */
    public String addRule(IRule rule) {
        /*IRuleAutomaton automaton;
        try {
            automaton = rule.getAutomaton(); // Try to compile rule automaton.
        } catch (RuleAutomatonException e) {
            logger.error("Could not compile automaton : " + rule + " (" + e.getMessage());

            throw new RuntimeException("Could not compile automaton : " + rule + " (" + e.getMessage());
        }*/
        IRuleAutomaton automaton = new RuleCompiler().compile(rule); // Try to compile rule automaton.

        rule.setName(rule.getName() == null ? rule.getClass().getSimpleName() + "-" + _ruleCounter++
                : rule.getName() + "-" + _ruleCounter++);

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

        // If the rule is valid, add it to the rule set, then register and observe the rule automaton.
        powerset.registerPatternObserver(this);
        _rules.put(rule.getName(), rule);
        _automata.put(rule.getName(), powerset);

        logger.info("Rule " + rule.getName() + " added : " + rule);
        logger.debug("Powerset : " + powerset);

        return rule.getName();
    }

    /**
     * Get a rule by its name.
     *
     * @param ruleName The name of the rule to get.
     * @return The rule.
     */
    public IRule getRule(String ruleName) {
        return _rules.get(ruleName);
    }

    /**
     * Get an automaton by its rule name.
     *
     * @param ruleName The name of the rule.
     * @return The rule's automaton.
     */
    public IRuleAutomaton getRuleAutomaton(String ruleName) {
        return _automata.get(ruleName);
    }

    /**
     * Get the last detected pattern.
     *
     * @return The last detected pattern.
     */
    public Collection<IEvent> getLastPattern() {
        return _lastPattern;
    }

    /**
     * Remove a rule by its name.
     *
     * @param ruleName The name of the rule to remove.
     */
    public void removeRule(String ruleName) {
        _rules.remove(ruleName);
        _automata.remove(ruleName);
    }

    /**
     * Remove all rules from the rule set.
     */
    public void removeAllRules() {
        _rules.clear();
        _automata.clear();
    }

    public void dispatchEvent(IEvent event) {
        _automata.values().forEach(automaton -> automaton.fire(event));
    }

    @Override
    public void notifyPattern(IRuleAutomaton ruleAutomaton, Collection<IEvent> pattern) {
        _lastPattern.clear();
        _lastPattern.addAll(pattern);

        logger.info("Pattern found : " + pattern);
    }

    @Override
    public String toString() {
        return _rules.toString();
    }
}