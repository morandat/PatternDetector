package fr.labri.patterndetector.rules;

/**
 * Created by William Braik on 6/22/2015.
 */
public interface IBinaryRule extends IRule {

    public IRule getLeftRule();

    public IRule getRightRule();
}
