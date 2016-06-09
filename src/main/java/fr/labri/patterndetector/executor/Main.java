package fr.labri.patterndetector.executor;

import fr.labri.patterndetector.automaton.IRuleAutomaton;
import fr.labri.patterndetector.executor.predicates.FieldAtom;
import fr.labri.patterndetector.executor.predicates.FieldKleeneDynamicIndex;
import fr.labri.patterndetector.executor.predicates.IntPredicateArity2;
import fr.labri.patterndetector.rule.visitors.RuleAutomatonMaker;
import fr.labri.patterndetector.rule.visitors.RuleNamer;
import fr.labri.patterndetector.rule.visitors.RulePrinter;
import fr.labri.patterndetector.rule.*;

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

        IRule k = new FollowedBy("a",
                new FollowedBy(
                        new Kleene("b")
                                .addPredicate(new IntPredicateArity2(
                                        new FieldAtom("0", "x"),
                                        new FieldKleeneDynamicIndex("1", "x", i -> i),
                                        (x, y) -> x.getValue() == y.getValue())),
                        "c"));

        RuleNamer.nameRules(k);
        RulePrinter.printRule(System.out, k);
    }
}
