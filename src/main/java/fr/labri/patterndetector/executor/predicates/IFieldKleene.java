package fr.labri.patterndetector.executor.predicates;

import java.util.function.IntFunction;

/**
 * Created by wbraik on 08/06/16.
 */
public interface IFieldKleene extends IField {

    IntFunction<Integer> getIndexFunc();
}
