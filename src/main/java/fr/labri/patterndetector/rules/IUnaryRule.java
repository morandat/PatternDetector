package fr.labri.patterndetector.rules;

/**
 * Created by William Braik on 6/28/2015.
 * <p>
 * Base interface for unary rules.
 */
public interface IUnaryRule extends IRule {

    IRule getRule();
}
