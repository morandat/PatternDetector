package fr.labri.patterndetector.rules;

import java.util.function.Predicate;

/**
 * Created by William Braik on 6/27/2015.
 * <p>
 * The elementary component of a pattern, which represents an event of a given type.
 */
public interface IAtom extends IRule {

    /**
     * Get the type of the event represented by the atom.
     *
     * @return The type of the event represented by the atom.
     */
    String getEventType();

    /**
     * Set a predicate for a given data field of the event represented by the atom.
     *
     * @param field     The field to set the predicate on.
     * @param predicate The predicate of the field.
     * @return The atom itself.
     */
    IAtom setPredicateOnField(String field, Predicate<Integer> predicate); // TODO replace Integer by generic type '?'
}
