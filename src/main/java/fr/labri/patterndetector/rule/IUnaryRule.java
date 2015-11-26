package fr.labri.patterndetector.rule;

/**
 * Created by William Braik on 6/28/2015.
 * <p>
 * Base interface for unary rules.
 */
public interface IUnaryRule extends ICompositeRule {

    IRule getChildRule();
}
