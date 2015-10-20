package fr.labri.patterndetector.rules;

/**
 * Created by William Braik on 6/28/2015.
 * <p>
 * Rules / operators with arity one (Kleene).
 */
public interface IUnaryRule extends IRule {

    IRule getRule();
}
