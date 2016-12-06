package fr.labri.patterndetector.runtime.expressions.builtins;

import fr.labri.patterndetector.runtime.expressions.*;

/**
 * Created by morandat on 05/12/2016.
 */
@Register(name = "=")
public class Equals extends IPredicate.BinaryStringPredicate {
    public Equals(IField... fields) {
        super(fields);
    }

    @Override
    public Boolean evaluate(String first, String second) {
        return first.equals(second);
    }

    @Override
    public Boolean evaluate(double first, double second) {
        return first == second;
    }

    @Override
    public Boolean evaluate(long first, long second) {
        return first == second;
    }
}
