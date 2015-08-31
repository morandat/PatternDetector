package fr.labri.patterndetector.rules;

/**
 * Created by William Braik on 6/25/2015.
 */
public enum RuleType { // TODO remove , use instanceof instead

    RULE_ATOM, RULE_ATOM_NOT, // Atom
    RULE_KLEENE_CONTIGUOUS, RULE_KLEENE, // Unary
    RULE_FOLLOWED_BY, RULE_FOLLOWED_BY_CONTIGUOUS, RULE_AND, RULE_OR // Binary
}
