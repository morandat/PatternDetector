package fr.labri.patterndetector.rule;

/**
 * Created by wbraik on 23/10/15.
 * <p>
 * Base interface for terminal rules, i.e. rules that cannot be decomposed into smaller rules.
 */
public interface ITerminalRule extends IRule {
    /**
     * Get the type of the event represented by the atom.
     *
     * @return The type of the event represented by the atom.
     */
    String getEventType();
}
