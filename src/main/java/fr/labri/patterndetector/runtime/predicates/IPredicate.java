package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.types.IValue;

import java.util.ArrayList;

/**
 * Created by wbraik on 5/18/2016.
 */
public interface IPredicate {

    IField[] getFields();

    boolean eval(IValue<?>... values);
}
