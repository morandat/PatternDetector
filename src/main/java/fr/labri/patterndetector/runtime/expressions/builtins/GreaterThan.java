package fr.labri.patterndetector.runtime.expressions.builtins;

import fr.labri.patterndetector.runtime.expressions.IField;
import fr.labri.patterndetector.runtime.expressions.IPredicate;
import fr.labri.patterndetector.runtime.expressions.Register;

/**
 * Created by morandat on 05/12/2016.
 */
@Register(name = ">")
public class GreaterThan extends IPredicate.BinaryNumberPredicate {
    public GreaterThan(IField... fields) {
        super(fields);
    }

    @Override
    public Boolean evaluate(double first, double second) {
        return first > second;
    }

    @Override
    public Boolean evaluate(long first, long second) {
        return first > second;
    }

    @Override
    public Boolean evaluate(String first, String second) {
        return first.compareTo(second) > 0;
    }
}
