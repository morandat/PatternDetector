package fr.labri.patterndetector.runtime.expressions.arithmetic;

import fr.labri.patterndetector.runtime.expressions.Expression;
import fr.labri.patterndetector.runtime.expressions.IField;
import fr.labri.patterndetector.runtime.expressions.Register;
import fr.labri.patterndetector.runtime.types.DoubleValue;
import fr.labri.patterndetector.runtime.types.IValue;
import fr.labri.patterndetector.runtime.types.LongValue;
import fr.labri.patterndetector.runtime.types.StringValue;

/**
 * Created by morandat on 05/12/2016.
 */
@Register(name = "+")
public class Add extends Expression {
    public Add(IField... fields) {
        super(fields);
    }

    @Override
    protected IValue<?> defaultValue() {
        return LongValue.ZERO;
    }

    @Override
    public IValue<?> evaluate(String first, String second) {
        return StringValue.from(first.concat(second));
    }

    @Override
    public IValue<?> evaluate(double first, double second) {
        return DoubleValue.from(first + second);
    }

    @Override
    public IValue<?> evaluate(long first, long second) {
        return LongValue.from(first + second);
    }
}
