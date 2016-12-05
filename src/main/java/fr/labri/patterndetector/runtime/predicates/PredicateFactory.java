package fr.labri.patterndetector.runtime.predicates;

/**
 * Created by morandat on 05/12/2016.
 */
public interface PredicateFactory {
    IPredicate instanciate(IField[] fields) throws IPredicate.InvalidPredicate;
}
