package fr.labri.patterndetector.executor;
/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Keeps references to the active rules and dispatches events to the rule automata.
 * TODO Triggers an action when a pattern is observed by an automaton.
 */

import fr.labri.patterndetector.automaton.IRuleAutomaton;
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
     * Add a rule to the rule set (only if it is valid).
     *
     * @param rule The rule to add.
     */
    public void addRule(IRule rule) { // TODO throw InvalidRuleException
        if (rule.getAutomaton() == null) { // Could not obtain the rule's automaton, invalid rule
            throw new RuntimeException("Invalid rule : " + rule);
        }

        rule.setName(rule.getName() == null ?
                rule.getClass().getSimpleName() + "-" + _ruleCounter++
                : rule.getName() + "-" + _ruleCounter++);

        IRuleAutomaton powerset = rule.getAutomaton().powerset();

        try {
            validateRuleAutomaton(powerset);
        } catch (Exception e) { // FIXME remove catch block
            throw new RuntimeException("Invalid rule " + rule.getName() + " (" + rule + ") : " + e.getMessage());
        }

        // If the rule is valid, add it to the rule set
        _rules.put(rule.getName(), rule);
        powerset.registerPatternObserver(this);
        _automata.put(rule.getName(), powerset);

        logger.info("Rule " + rule.getName() + " added : " + rule);
        logger.debug("Powerset : " + powerset);
    }

    /**
     * Get a rule by its name.
     *
     * @param ruleName The name of the rule to get.
     * @return The rule.
     */
    public IRule getRuleByName(String ruleName) {
        return _rules.get(ruleName);
    }

    /**
     * Get an automaton by its rule name.
     *
     * @param ruleName The name of the rule.
     * @return The rule's automaton.
     */
    public IRuleAutomaton getRuleAutomatonByName(String ruleName) {
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

    /**
     * Detect patterns in a stream of events.
     *
     * @param events The stream of events.
     */
    public void detect(Collection<IEvent> events) {
        logger.info("Detecting patterns in stream : " + events);

        // Each rule automaton fires the events in the order of the stream
        events.stream().forEach(event -> _automata.values().forEach(automaton -> {
            try {
                automaton.fire(event);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }));
    }

    /**
     * Check the validity of a rule automaton.
     *
     * @param automaton The automaton to validate.
     * @throws Exception
     */
    private void validateRuleAutomaton(IRuleAutomaton automaton) throws Exception { //TODO vraie exception
        if (automaton.getFinalState() == null) {
            throw new Exception("Rule doesn't terminate !");
        } else if (automaton.getFinalState().getTransitions().size() > 0) {
            throw new Exception("Rule is ambiguous !");
        }
    }

    @Override
    public void notifyPattern(IRuleAutomaton ruleAutomaton, Collection<IEvent> pattern) {
        _lastPattern.clear();
        _lastPattern.addAll(pattern);
        IRule rule = ruleAutomaton.getRule();

        logger.info("Pattern found by " + rule.getName() + " : " + rule);
        logger.info("Pattern found : " + pattern);
    }

    @Override
    public String toString() {
        return _rules.toString();
    }
}
