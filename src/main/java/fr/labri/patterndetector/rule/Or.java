package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.rule.visitors.IRuleVisitor;

/**
 * Created by will on 29/11/15.
 * <p>
 * TODO must check that left and right rules are both valid.
 * TODO If one of them is invalid, then the Or rule is invalid.
 * TODO Ex: if one of the rules is a+ (non-terminating), Or is invalid.
 */
public class Or extends AbstractBinaryRule {

    public static final String Symbol = "|";

    public Or(IRule left, IRule right) {
        super(Or.Symbol, left, right);
    }

    public Or(String e, IRule right) {
        super(Or.Symbol,
                new Atom(e),
                right);
    }

    public Or(IRule left, String e) {
        super(Or.Symbol,
                left,
                new Atom(e));
    }

    public Or(String e1, String e2) {
        super(Or.Symbol,
                new Atom(e1),
                new Atom(e2));
    }

    public void accept(IRuleVisitor visitor) {
        visitor.visit(this);
    }
}
