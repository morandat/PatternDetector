package fr.labri.patterndetector.rule.visitors;

import fr.labri.patterndetector.rule.*;

import java.io.PrintStream;

/**
 * Created by wbraik on 20/11/15.
 * <p>
 * Used to printRule rules.
 */
public class RulePrinter {

    PrintStream _out;

    public RulePrinter(PrintStream out) {
        _out = out;
    }

    public void printRule(IRule rule) {
        _out.println(new RuleStringifier().stringify(rule));
    }
}
