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
    private int _ruleId = 0;
    private Map<IRule, IRuleAutomaton> _ruleAutomata = new HashMap<>(); // Map binding each rule to its automaton
    private Collection<IEvent> _patternHistory = new ArrayList<>();

    private RuleManager() {

    }

    /**
     * Get the unique instance of the RuleManager (Singleton)
     *
     * @return the instance of RuleManager
     */
    public static RuleManager getInstance() {
        if (_instance == null) {
            _instance = new RuleManager();
        }
        return _instance;
    }

    /**
     * Add a rule to the rule set
     *
     * @param rule The rule to add
     */
    public void addRule(IRule rule) {
        if (rule.getAutomaton() == null) {
            System.err.println("Invalid rule : " + rule);
        } else {
            rule.setName(rule.getName() == null ?
                    rule.getClass().getSimpleName() + "-" + _ruleId++ : rule.getName() + "-" + _ruleId++);

            IRuleAutomaton powerset = AutomatonUtils.powerset(rule.getAutomaton());
            checkRuleAutomaton(powerset);

            _ruleAutomata.put(rule, powerset);
            System.out.println("* Rule " + rule.getName() + " added : " + rule);
            System.out.println(rule.getName() + "'s powerset : " + _ruleAutomata.get(rule));
        }
    }

    public IRule getRule(String ruleName) {
        for (IRule rule : _ruleAutomata.keySet()) {
            if (rule.getName().equals(ruleName)) {
                return rule;
            }
        }

        return null;
    }

    public Set<IRule> getRules() {
        return _ruleAutomata.keySet();
    }

    public IRuleAutomaton getRuleAutomaton(String ruleName) {
        IRule rule = getRule(ruleName);

        if (rule != null) {
            return _ruleAutomata.get(rule);
        } else {
            return null;
        }
    }

    public Collection<IEvent> getPatternHistory() {
        return _patternHistory;
    }

    /**
     * Remove a rule by name
     *
     * @param ruleName The name of the rule to remove.
     */
    public void removeRule(String ruleName) {
        _ruleAutomata.keySet().forEach(rule -> {
            if (rule.getName().equals(ruleName)) {
                _ruleAutomata.remove(rule);
                System.out.println("* Rule " + rule.getName() + " removed.");
            }
        });
    }

    /**
     * Remove all rules from the rule set
     */
    public void removeAllRules() {
        _ruleAutomata.clear();
    }

    public void detect(Collection<IEvent> events) {
        System.out.println("\n* Stream : " + events + "\n");

        events.stream().forEach(event -> _ruleAutomata.values().forEach(automaton -> {
            try {
                automaton.fire(event);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }));
    }

    public void notifyPattern(Collection<IEvent> pattern, IRule rule) {
        _patternHistory.addAll(pattern); // TODO for testing purposes
        System.out.println("*** PATTERN FOUND : " + pattern + " ***");
    }

    private void checkRuleAutomaton(IRuleAutomaton automaton) {
        if (automaton.getFinalState() == null) {
            System.err.println("Warning : rule " + automaton.getRuleName() + " doesn't terminate !");
        } else if (automaton.getFinalState().getTransitions().size() > 0) {
            System.err.println("Warning : rule " + automaton.getRuleName() + " is ambiguous !");
        }
    }
}
