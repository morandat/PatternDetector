package fr.labri.patterndetector.runtime.expressions.builtins;

import fr.labri.patterndetector.runtime.expressions.BinaryOperation;
import fr.labri.patterndetector.runtime.expressions.IExpression;
import fr.labri.patterndetector.runtime.expressions.IField;
import fr.labri.patterndetector.runtime.expressions.IValue;

/**
 * Created by morandat on 05/12/2016.
 */
@Register(name = "+")
public class Add extends BinaryOperation.Arithmetic<IValue<?>> implements IExpression {
    public Add(IField... fields) {
        super(fields);
    }

    @Override
    protected IValue<?> defaultValue() {
        return IValue.LongValue.ZERO;
    }

    @Override
    public IValue<?> evaluate(String first, String second) {
        return IValue.StringValue.from(first.concat(second));
    }

    @Override
    public IValue<?> evaluate(double first, double second) {
        return IValue.DoubleValue.from(first + second);
    }

    @Override
    public IValue<?> evaluate(long first, long second) {
        return IValue.LongValue.from(first + second);
    }
}
