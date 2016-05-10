package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.rule.visitors.IRuleVisitor;

/**
 * Created by william.braik on 10/07/2015.
 * <p>
 * The Kleene rule captures at least one occurrence of an event type.
 * Because the stream of events is infinite, it does not have any termination criterion by default.
 * Therefore, the corresponding Kleene automaton does not have any final state.
 * However, Kleene can be connected to other rules using the FollowedBy operator.
 * In that case, Kleene terminates as soon as the following rule begins.
 * TODO It can also be used on its own, if a window is specified for the pattern.
 */
public class Kleene extends AbstractUnaryRule {

    public static final String Symbol = "+";

    public Kleene(IRule r) {
        super(Kleene.Symbol, r);
    }

    public Kleene(String e) {
        super(Kleene.Symbol,
                (e.startsWith("!") ? new AtomNot(e.substring(1)) : new Atom(e)));
    }

    @Override
    public void accept(IRuleVisitor visitor) {
        visitor.visit(this);
    }
}
