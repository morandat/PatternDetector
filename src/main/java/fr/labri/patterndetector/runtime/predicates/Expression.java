package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.Matchbuffer;
import fr.labri.patterndetector.runtime.UnknownFieldException;
import fr.labri.patterndetector.runtime.types.IValue;

import java.util.Optional;

/**
 * Created by morandat on 05/12/2016.
 */
public abstract class Expression extends BinaryOperation<IValue<?>> implements IField {

    public Expression(IField... fields) {
        super(fields);
    }

    @Override
    public Optional<IValue<?>> resolve(Matchbuffer matchbuffer, Event currentEvent) throws UnknownFieldException {
        IField[] fields = getFields();
        Optional<IValue<?>> left = fields[0].resolve(matchbuffer, currentEvent);
        Optional<IValue<?>> right = fields[0].resolve(matchbuffer, currentEvent);
        if (left.isPresent() && right.isPresent())
            return Optional.of(evaluate(left.get(), right.get()));
        return Optional.empty();
    }
}
