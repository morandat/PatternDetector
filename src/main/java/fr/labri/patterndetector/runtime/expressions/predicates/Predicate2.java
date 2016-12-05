package fr.labri.patterndetector.runtime.expressions.predicates;

import fr.labri.patterndetector.runtime.expressions.BinaryOperation;
import fr.labri.patterndetector.runtime.expressions.IField;
import fr.labri.patterndetector.runtime.expressions.IPredicate;
import fr.labri.patterndetector.runtime.types.IValue;


/**
 * Created by morandat on 02/12/2016.
 */
public abstract class Predicate2 extends BinaryOperation<Boolean> implements IPredicate {
    public Predicate2(IField... fields) {
        super(fields);
    }

    @Override
    protected Boolean defaultValue() {
        return false;
    }
    public boolean eval(IValue<?>... values) {
        assert values.length == 2;
        return evaluate(values[0], values[1]);
    }
}
