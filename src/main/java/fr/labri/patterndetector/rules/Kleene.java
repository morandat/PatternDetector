package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

/**
 * Created by william.braik on 10/07/2015.
 */

/**
 * This rule does not have any termination criterion. The automaton therefore does not have any final state.
 * A way to make this operator terminate is to append an atom (or a rule that terminates) at the end of this rule ;
 * Or to specify a time constraint on the atom.
 * Ex : a+ doesn't terminate but a+ -> b, or a+|10| terminates.
 */
public class Kleene extends AbstractUnaryRule {

    public static final String Symbol = "+";

    private String _pivotStateLabel;

    public Kleene(IRule r) {
        super(RuleType.RULE_KLEENE, Kleene.Symbol, r);
    }

    public Kleene(String e) {
        super(RuleType.RULE_KLEENE, Kleene.Symbol, new Atom(e));
    }

    // TODO add a parameter for max buffer size ? ex : a+(2) would return [aa]

    public void buildAutomaton() throws Exception {
        IRuleAutomaton base = AutomatonUtils.copy(_rule.getAutomaton());

        IRuleAutomaton automaton = new RuleAutomaton(this);

        // base component
        automaton.registerInitialState(base.getInitialState());
        base.getStates().values().forEach(automaton::registerState);
        IState q = base.getFinalState();
        q.setFinal(false);
        automaton.registerState(q);

        // add extra stuff to obtain the new automaton (Thompson's construction style)
        IState s = new State();
        q.registerTransition(s, Transition.LABEL_EPSILON, TransitionType.TRANSITION_DROP);
        s.registerTransition(s, Transition.LABEL_NEGATION, TransitionType.TRANSITION_DROP);
        s.registerTransition(automaton.getInitialState(), Transition.LABEL_EPSILON, TransitionType.TRANSITION_DROP);
        automaton.registerState(s);

        _pivotStateLabel = s.getLabel();
        _automaton = automaton;
    }

    public String getPivotStateLabel() {
        return _pivotStateLabel;
    }
}
