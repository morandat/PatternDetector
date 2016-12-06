package fr.labri.patterndetector.runtime.expressions;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.Matchbuffer;
import fr.labri.patterndetector.runtime.UnknownFieldException;
import fr.labri.patterndetector.runtime.types.IValue;

import java.util.Optional;

/**
 * Created by morandat on 06/12/2016.
 */
public interface IExpression extends IField {

    IField[] getFields();

    default Optional<IValue<?>> fetch(Matchbuffer matchbuffer, Event currentEvent) throws UnknownFieldException {
        IField[] fields = getFields();
        Optional<IValue<?>> left = fields[0].fetch(matchbuffer, currentEvent);
        Optional<IValue<?>> right = fields[0].fetch(matchbuffer, currentEvent);
        if (left.isPresent() && right.isPresent())
            return Optional.of(evaluate(left.get(), right.get()));
        return Optional.empty();
    }

    IValue<?> evaluate(IValue<?> iValue, IValue<?> iValue1);
}
