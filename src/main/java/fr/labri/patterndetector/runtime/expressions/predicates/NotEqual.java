package fr.labri.patterndetector.runtime.expressions.predicates;

import fr.labri.patterndetector.runtime.expressions.IField;
import fr.labri.patterndetector.runtime.expressions.Register;

/**
 * Created by morandat on 05/12/2016.
 */
@Register(name = "=")
public class NotEqual extends Predicate2 {
    public NotEqual(IField... fields) {
        super(fields);
    }

    @Override
    public Boolean evaluate(String first, String second) {
        return !first.equals(second);
    }

    @Override
    public Boolean evaluate(double first, double second) {
        return first != second;
    }

    @Override
    public Boolean evaluate(long first, long second) {
        return first != second;
    }
}
