package fr.labri.patterndetector.executor.predicates;

import fr.labri.patterndetector.types.IValue;

import java.util.ArrayList;

/**
 * Created by wbraik on 5/18/2016.
 */
public interface IPredicate {

    ArrayList<String> getFields();

    boolean eval(IValue<?>... values);
}
