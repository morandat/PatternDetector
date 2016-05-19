package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.executor.predicates.IntPredicateArity1;
import fr.labri.patterndetector.executor.predicates.StringPredicateArity1;
import fr.labri.patterndetector.rule.visitors.RuleAutomatonMaker;
import fr.labri.patterndetector.rule.visitors.RuleNamer;
import fr.labri.patterndetector.rule.visitors.RulePrinter;
import fr.labri.patterndetector.rule.*;
import fr.labri.patterndetector.types.IntegerValue;
import fr.labri.patterndetector.types.StringValue;

/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Main class for quick testing.
 * Use the JUnit test suite for true testing.
 */
public class Main {

    public static void main(String[] args) {
        /*IRule r = new FollowedBy("q",
                new FollowedBy(
                        new Atom("f").setAction(() -> System.out.println("Fiche produit !")),
                        "a"));*/

        IRule r = new FollowedBy(
                new Atom("a").setAction(() -> System.out.println("HELLO A!")),
                new Atom("b").setAction(() -> System.out.println("HELLO B!")));

        RuleNamer.nameRules(r);
        RulePrinter.printRule(System.out, r);

        IRuleAutomaton nfa = RuleAutomatonMaker.makeAutomaton(r);
        System.out.println(nfa);
        IRuleAutomaton dfa = nfa.powerset();
        System.out.println(dfa);
    }
}
