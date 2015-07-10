package fr.labri.patterndetector.rules;

/**
 * Created by William Braik on 6/25/2015.
 */
public enum RuleType {

    RULE_ATOM, RULE_ATOM_NOT, // Atom
    RULE_KLEENE, // Unary
    RULE_FOLLOWED_BY, RULE_NOT_FOLLOWED_BY, RULE_AND, RULE_OR // Binary
}
