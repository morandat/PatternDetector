package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

/**
 * Created by william.braik on 10/07/2015.
 */

public class KleeneContiguous extends AbstractUnaryRule {

    public static final String Symbol = "+.";

    public KleeneContiguous(IRule r) {
        super(RuleType.RULE_KLEENE_CONTIGUOUS, KleeneContiguous.Symbol, r);

        try {
            buildAutomaton();
        } catch (Exception e) {
            System.err.println("Can't instantiate rule ! (" + e.getMessage() + ")");
        }
    }

    public void buildAutomaton() throws Exception {
        IRuleAutomaton base = AutomatonUtils.copy(_r.getAutomaton());

        IRuleAutomaton automaton = new RuleAutomaton(this);

        // base component
        automaton.registerInitialState(base.getInitialState());
        base.getStates().values().forEach(automaton::registerState);
        IState q = base.getFinalState();
        q.setFinal(false);
        automaton.registerState(q);

        // add extra stuff to obtain the new automaton (Thompson's construction style)
        q.registerTransition(automaton.getInitialState(), Transition.LABEL_EPSILON, TransitionType.TRANSITION_DROP);

        IState f = new State();
        automaton.registerFinalState(f);
        q.registerTransition(f, Transition.LABEL_NEGATION, TransitionType.TRANSITION_DROP);

        _automaton = automaton;
    }
}
