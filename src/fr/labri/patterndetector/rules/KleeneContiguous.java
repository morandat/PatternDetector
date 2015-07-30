package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.automaton.*;

/**
 * Created by william.braik on 10/07/2015.
 */

public class KleeneContiguous extends AbstractUnaryRule {

    public KleeneContiguous(IRule r) {
        super(RuleType.RULE_KLEENE, ".+", r);

        try {
            buildAutomaton();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void buildAutomaton() throws Exception {
        IAutomaton base = AutomatonUtils.copy(_r.getAutomaton());
        IAutomaton kleene = AutomatonUtils.copy(_r.getAutomaton());

        System.out.println("Base component : " + base);
        System.out.println("Kleene component : " + base);

        IAutomaton automaton = new Automaton();

        // base component
        automaton.registerInitialState(base.getInitialState());
        base.getStates().values().forEach(automaton::registerState);
        IState q = base.getFinalState();
        q.setFinal(false);
        automaton.registerState(q);

        // kleene component
        // Merge p and q together (copy transitions of p and add them to q)
        IState p = kleene.getInitialState();
        kleene.getStates().values().forEach(automaton::registerState);
        p.getTransitions().values().forEach(t -> {
            try {
                q.registerTransition(t.getTarget(), t.getLabel(), t.getType());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
        IState r = kleene.getFinalState();
        r.setFinal(false);
        automaton.registerState(r);

        // add extra stuff to obtain the new automaton (Thompson's construction style)
        IState f = new State();
        automaton.registerFinalState(f);
        r.registerTransition(q, Transition.LABEL_EPSILON, TransitionType.TRANSITION_DROP);
        q.registerTransition(f, Transition.LABEL_NEGATION, TransitionType.TRANSITION_DROP);

        System.out.println("Final automaton : " + automaton);

        _automaton = automaton;
    }
}
