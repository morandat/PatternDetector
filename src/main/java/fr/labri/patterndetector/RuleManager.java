package fr.labri.patterndetector;
/**
 * Created by William Braik on 6/22/2015.
 */

import fr.labri.patterndetector.automaton.AutomatonUtils;
import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.rules.IRule;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RuleManager {

    private int _ruleId;

    Map<IRule, IRuleAutomaton> _rules; // Map binding each rule to its automaton

    public RuleManager() {
        _ruleId = 0;
        _rules = new HashMap<>();
    }

    public void addRule(IRule rule) {
        if (rule.getAutomaton() == null) {
            System.err.println("Invalid rule : " + rule);
        } else {
            rule.setName(rule.getName() == null ?
                    rule.getClass().getSimpleName() + "-" + _ruleId++ : rule.getName() + "-" + _ruleId++);
            _rules.put(rule, AutomatonUtils.powerset(rule.getAutomaton()));
            System.out.println("* Rule " + rule.getName() + " added : " + rule);
            System.out.println(rule.getName() + " powerset : " + _rules.get(rule));
        }
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
}
