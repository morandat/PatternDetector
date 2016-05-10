package fr.labri.patterndetector.rule;

/**
 * Created by william.braik on 08/07/2015.
 * <p>
 * A time constraint to be specified for a rule.
 */
public interface ITimeConstraint {

    /**
     * Get the time constraint's value.
     *
     * @return The time constraint's value.
     */
    int getValue();
}
