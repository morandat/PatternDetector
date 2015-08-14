package fr.labri.patterndetector;
/**
 * Created by William Braik on 6/22/2015.
 */

import fr.labri.patterndetector.automaton.AutomatonUtils;
import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.rules.IRule;

import java.util.*;

public final class RuleManager {

    private static RuleManager _instance = null;
    private int _ruleId = 0;
    private Map<IRule, IRuleAutomaton> _rules = new HashMap<>(); // Map binding each rule to its automaton
    private Collection<IEvent> _patternHistory = new ArrayList<>();

    private RuleManager() {

    }

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
            check(powerset);

            _rules.put(rule, powerset);
            System.out.println("* Rule " + rule.getName() + " added : " + rule);
            System.out.println(rule.getName() + " powerset : " + _rules.get(rule));
        }
    }

    public IRule getRule(String ruleName) {
        for (IRule rule : _rules.keySet()) {
            if (rule.getName().equals(ruleName)) {
                return rule;
            }
        }

        return null;
    }

    public Set<IRule> getRules() {
        return _rules.keySet();
    }

    public IRuleAutomaton getRuleAutomaton(String ruleName) {
        IRule rule = getRule(ruleName);

        if (rule != null) {
            return _rules.get(rule);
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
        _rules.keySet().forEach(rule -> {
            if (rule.getName().equals(ruleName)) {
                _rules.remove(rule);
                System.out.println("* Rule " + rule.getName() + " removed.");
            }
        });
    }

    /**
     * Remove all rules from the rule set
     */
    public void removeAllRules() {
        _rules.clear();
    }

    public void detect(Collection<Event> events) {
        System.out.println("\n* Stream : " + events + "\n");

        events.stream().forEach(event -> _rules.values().forEach(automaton -> {
            try {
                automaton.fire(event);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }));
    }

    public void notifyPattern(Collection<IEvent> pattern) {
        _patternHistory.addAll(pattern);
        System.out.println("*** PATTERN FOUND : " + pattern + " ***");
    }

    private void check(IRuleAutomaton automaton) {
        if (automaton.getFinalState().getTransitions().size() > 0) {
            System.err.println("RULE IS AMBIGUOUS !");
        }
    }
}
