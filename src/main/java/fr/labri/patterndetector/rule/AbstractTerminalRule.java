package fr.labri.patterndetector.rule;

/**
 * Created by wbraik on 26/11/15.
 */
public abstract class AbstractTerminalRule extends AbstractRule implements ITerminalRule {

    public AbstractTerminalRule(String symbol) {
        super(symbol);
    }
}
