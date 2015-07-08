package fr.labri.patterndetector.rules;

import java.util.concurrent.TimeUnit;

/**
 * Created by william.braik on 08/07/2015.
 */
public interface ITimeConstraint {

    public int getValue();

    public TimeUnit getUnit();
}
