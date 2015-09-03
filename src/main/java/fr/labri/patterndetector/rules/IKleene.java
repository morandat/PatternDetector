package fr.labri.patterndetector.rules;

/**
 * Created by william.braik on 01/09/2015.
 */
public interface IKleene extends IRule {

    /**
     * Kleene-type rules have the option to specify a maximum length for the sequence they recognize.
     *
     * @param maxSeqSize The maximum length of the sequence recognized by this rule.
     * @return The rule itself.
     */
    IRule setMaxSeqSize(int maxSeqSize);
}
