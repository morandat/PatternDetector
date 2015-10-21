package fr.labri.patterndetector.rules;

/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Base interface for binary rules.
 */
public interface IBinaryRule extends IRule {

    IRule getLeftRule();

    IRule getRightRule();
}
