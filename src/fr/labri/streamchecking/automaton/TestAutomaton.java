package fr.labri.streamchecking.automaton;

import fr.labri.streamchecking.EventType;

/**
 * Created by William Braik on 6/29/2015.
 */
public class TestAutomaton {

    public static void main(String[] args) {

        IState s0 = new State(StateType.STATE_INITIAL, "0");
        IState s1 = new State(StateType.STATE_NORMAL, "1");
        IState s2 = new State(StateType.STATE_FINAL, "2");

        s0.registerTransition(EventType.EVENT_A, s1);
        s1.registerTransition(EventType.EVENT_A, s1);
        s1.registerTransition(EventType.EVENT_C, s1);
        s1.registerTransition(EventType.EVENT_B, s2);

        // a --> b
        IAutomaton a1 = new Automaton();
        a1.setInitialState(s0);
        a1.registerState(s1);
        a1.registerState(s2);

        try {
            System.out.println(a1.getCurrentState());
            System.out.println(a1.fire(EventType.EVENT_A));
            System.out.println(a1.fire(EventType.EVENT_A));
            System.out.println(a1.fire(EventType.EVENT_C));
            System.out.println(a1.fire(EventType.EVENT_B));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            //e.printStackTrace();
        }
    }
}