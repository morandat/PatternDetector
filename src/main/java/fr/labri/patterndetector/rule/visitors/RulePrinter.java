package fr.labri.patterndetector.rule.visitors;

import fr.labri.patterndetector.rule.*;

import java.io.PrintStream;

/**
 * Created by wbraik on 20/11/15.
 * <p>
 * Used to printRule rules.
 */
public final class RulePrinter {

    private RulePrinter() {
    }

    public static void printRule(PrintStream out, IRule rule) {
        out.println(RuleStringifier.stringify(rule));
    }
}
