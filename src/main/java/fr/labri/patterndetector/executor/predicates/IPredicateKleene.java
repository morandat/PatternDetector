package fr.labri.patterndetector.executor.predicates;

import java.util.ArrayList;
import java.util.function.IntFunction;

/**
 * Created by wbraik on 07/06/16.
 */
public interface IPredicateKleene extends IPredicate {

    ArrayList<IntFunction<Integer>> getIndexFunctions();
}
