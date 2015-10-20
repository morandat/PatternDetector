package fr.labri.patterndetector;
/**
 * Created by William Braik on 6/22/2015.
 */

import fr.labri.patterndetector.automaton.AutomatonUtils;
import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.rules.IRule;

import java.util.*;

public final class RuleManager {

    private static RuleManager _instance = null; // Singleton

    private int _ruleCounter = 0; // Internal rule counter, used to build rule IDs
    private Map<String, IRule> _rules = new HashMap<>(); // Maps names to rules
    private Map<String, IRuleAutomaton> _automata = new HashMap<>(); // Maps rule names to automata
    private Collection<IEvent> _lastPattern = new ArrayList<>(); // Last detected pattern

    private RuleManager() {
    }

    /**
     * Get the singleton instance of RuleManager.
     *
     * @return The instance of RuleManager.
     */
    public static RuleManager getInstance() {
        if (_instance == null) {
            _instance = new RuleManager();
        }

        return _instance;
    }

    /**
     * Add a rule to the rule set (only if it is valid).
     *
     * @param rule The rule to add.
     */
    public void addRule(IRule rule) {
        if (rule.getAutomaton() == null) { // Could not obtain the rule's automaton, invalid rule
            System.err.println("Invalid rule : " + rule);
        } else {
            rule.setName(rule.getName() == null ?
                    rule.getClass().getSimpleName() + "-" + _ruleCounter++
                    : rule.getName() + "-" + _ruleCounter++);

            IRuleAutomaton powerset = AutomatonUtils.powerset(rule.getAutomaton());
            _automata.put(rule.getName(), powerset);

            try {
                validateRuleAutomaton(powerset);
            } catch (Exception e) {
                System.err.println("Invalid rule " + rule.getName() + " (" + rule + ") : " + e.getMessage());
            }

            // If the rule is valid, add it to the rule set
            _rules.put(rule.getName(), rule);
            System.out.println("* Rule " + rule.getName() + " (" + rule + ") " + "added : " + rule);
            System.out.println("Powerset : " + powerset);
        }
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
        System.out.println("\n* Stream : " + events + "\n");

        // Each rule's automaton fires the events in the order of the stream
        events.stream().forEach(event -> _automata.values().forEach(automaton -> {
            try {
                automaton.fire(event);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }));
    }

    /**
     * A rule's automaton calls this method to notify the detection of a pattern to the RuleManager.
     *
     * @param pattern The pattern found.
     * @param rule    The rule which found the pattern.
     */
    public void notifyPattern(Collection<IEvent> pattern, IRule rule) {
        _lastPattern.clear();
        _lastPattern.addAll(pattern);
        System.out.println("*** PATTERN FOUND BY " + rule.getName() + " (" + rule + ")" + " : " + pattern + " ***");
    }

    /**
     * Check the validity of a rule automaton.
     *
     * @param automaton The automaton to validate.
     * @throws Exception
     */
    private void validateRuleAutomaton(IRuleAutomaton automaton) throws Exception {
        if (automaton.getFinalState() == null) {
            throw new Exception("Rule doesn't terminate !");
        } else if (automaton.getFinalState().getTransitions().size() > 0) {
            throw new Exception("Rule is ambiguous !");
        }
    }

    @Override
    public String toString() {
        return _rules.toString();
    }
}
