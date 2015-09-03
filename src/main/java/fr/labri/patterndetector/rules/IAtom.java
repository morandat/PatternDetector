package fr.labri.patterndetector.rules;

import java.util.function.Predicate;

/**
 * Created by William Braik on 6/27/2015.
 */
public interface IAtom extends IRule {

    String getEventType();

    IAtom setPredicateOnField(String field, Predicate<Integer> predicate); // TODO replace Integer by generic type '?'
}
