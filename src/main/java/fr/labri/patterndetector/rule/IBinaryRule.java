package fr.labri.patterndetector.rule;

/**
 * Created by William Braik on 6/22/2015.
 * <p>
 * Base interface for binary rules.
 */
public interface IBinaryRule extends ICompositeRule {

    IRule getLeftChildRule();

    IRule getRightChildRule();
}
