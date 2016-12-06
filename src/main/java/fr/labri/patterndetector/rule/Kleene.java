package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.rule.visitors.IRuleVisitor;

/**
 * Created by william.braik on 10/07/2015.
 * <p>
 * The KleeneAccess rule captures at least one occurrence of an event type.
 * Because the stream of events is infinite, it does not have any termination criterion by default.
 * Therefore, the corresponding KleeneAccess automaton does not have any final state.
 * However, KleeneAccess can be connected to other rules using the FollowedBy operator.
 * In that case, KleeneAccess terminates as soon as the following rule begins.
 * TODO It can also be used on its own, if a window is specified for the pattern.
 */
public class Kleene extends AbstractKleene {

    public static final String Symbol = "+";

    public Kleene(String eventType) {
        super(Kleene.Symbol, eventType);
    }

    @Override
    public void accept(IRuleVisitor visitor) {
        visitor.visit(this);
    }
}
