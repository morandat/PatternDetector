package fr.labri.patterndetector.runtime.predicates.builtins;

import fr.labri.patterndetector.runtime.predicates.*;

/**
 * Created by morandat on 05/12/2016.
 */
@Predicate(name = "=")
public class Equal extends Predicate2 {
    public Equal(IField... fields) {
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
