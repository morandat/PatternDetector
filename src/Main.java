import java.util.ArrayList;

/**
 * Created by William Braik on 6/22/2015.
 */
public class Main {

    public static void main(String[] args) {

        ArrayList<Event> eventStream = Generator.generateStream();

        // 1) a --> b
        IRule r1 = new AlwaysFollowedBy(new Atom(EventType.EVENT_A), new Atom(EventType.EVENT_B));
        // 2) a <-- b
        IRule r2 = new AlwaysPrecedes(new Atom(EventType.EVENT_A), new Atom(EventType.EVENT_B));
        // 3) a -/->
        IRule r3 = new NeverFollowedBy(new Atom(EventType.EVENT_A), new Atom(EventType.EVENT_B));
        // 4) a && b
        IRule r4 = new And(new Atom(EventType.EVENT_A), new Atom(EventType.EVENT_B));
        // 5) a || b
        IRule r5 = new Or(new Atom(EventType.EVENT_A), new Atom(EventType.EVENT_B));
        // 6) !a
        IRule r6 = new Not(new Atom(EventType.EVENT_A));
        // 7) R --> b
        IRule r7 = new AlwaysFollowedBy(r1, new Atom(EventType.EVENT_B));
        // 8) R <-- b
        IRule r8 = new AlwaysPrecedes(r1, new Atom(EventType.EVENT_B));
        // 9) R -/-> b
        IRule r9 = new NeverFollowedBy(r1, new Atom(EventType.EVENT_B));
        // 10) a --> R
        IRule r10 = new AlwaysFollowedBy(new Atom(EventType.EVENT_A), r2);
        // 11) a <-- R
        IRule r11 = new AlwaysPrecedes(new Atom(EventType.EVENT_A), r2);
        // 12) a -/-> R
        IRule r12 = new NeverFollowedBy(new Atom(EventType.EVENT_A), r2);
        // 13) !c || ((a --> b) && (b <-- R))
        IRule r13 = new Or(
                new Not(new Atom(EventType.EVENT_C)),
                new And(
                        new AlwaysFollowedBy(new Atom(EventType.EVENT_A), new Atom(EventType.EVENT_B)),
                        new AlwaysPrecedes(new Atom(EventType.EVENT_A), r1)
                ));

        Detector detector = new Detector(r13);
        boolean res = detector.detect(eventStream);

        System.out.println(res);
    }
}
