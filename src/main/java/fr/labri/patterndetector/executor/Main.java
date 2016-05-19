package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.executor.predicates.IntPredicateArity1;
import fr.labri.patterndetector.executor.predicates.StringPredicateArity1;
import fr.labri.patterndetector.rule.visitors.RuleAutomatonMaker;
import fr.labri.patterndetector.rule.visitors.RuleNamer;
import fr.labri.patterndetector.rule.visitors.RulePrinter;
import fr.labri.patterndetector.rule.*;
import fr.labri.patterndetector.types.IValue;
import fr.labri.patterndetector.types.IntegerValue;
import fr.labri.patterndetector.types.StringValue;

import java.util.ArrayList;

/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Main class for quick testing.
 * Use the JUnit test suite for true testing.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println(new IntPredicateArity1("$0.x", x -> x.getValue() > 15)
                .eval(new IntegerValue(5)));

        System.out.println(new StringPredicateArity1("$0.x", x -> x.getValue().equals("chapeau"))
                .eval(new StringValue("chapeau")));

        IRule r = new FollowedBy(new FollowedBy("q", "f"), "a");
        RuleNamer.nameRules(r);
        RulePrinter.printRule(System.out, r);
    }
}
