package fr.labri.patterndetector.rules;

import fr.labri.patterndetector.Detector;
import fr.labri.patterndetector.Event;
import fr.labri.patterndetector.EventType;
import fr.labri.patterndetector.Generator;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by William Braik on 6/22/2015.
 */
public class TestRules {

    public static void main(String[] args) {
        // 1) a --> b
        IRule r1 = new FollowedBy(new Atom(EventType.EVENT_A), new Atom(EventType.EVENT_B));
        // 3) a -/->
        IRule r3 = new NotFollowedBy(new Atom(EventType.EVENT_A), new Atom(EventType.EVENT_B));
        // 4) a && b
        IRule r4 = new And(new Atom(EventType.EVENT_A), new Atom(EventType.EVENT_B));
        // 5) a || b
        IRule r5 = new Or(new Atom(EventType.EVENT_A), new Atom(EventType.EVENT_B));
        // 6) !a
        IRule r6 = new AtomNot(EventType.EVENT_A);
        // 7) R --> b
        IRule r7 = new FollowedBy(r1, new Atom(EventType.EVENT_B));
        // 9) R -/-> b
        IRule r9 = new NotFollowedBy(r1, new Atom(EventType.EVENT_B));
        // 10) a --> R
        IRule r10 = new FollowedBy(new Atom(EventType.EVENT_A), r1);
        // 12) a -/-> R
        IRule r12 = new NotFollowedBy(new Atom(EventType.EVENT_A), r1);
        // 13) !c || ((a --> b) && (b <-- R))
        IRule r13 = new Or(
                new AtomNot(EventType.EVENT_C),
                new And(
                        new FollowedBy(new Atom(EventType.EVENT_A), new Atom(EventType.EVENT_B)),
                        new FollowedBy(new Atom(EventType.EVENT_A), r1)
                ));
        // 14) a --> b [10s]
        IRule r14 = new FollowedBy(new Atom(EventType.EVENT_A), new Atom(EventType.EVENT_B),
                new TimeConstraint(10, TimeUnit.SECONDS));
        // 15) a+
        IRule r15 = new KleeneContiguous(new Atom(EventType.EVENT_A));
        // 16) a++
        IRule r16 = new KleeneNotContiguous(new Atom(EventType.EVENT_A));
        // 17) a . b
        IRule r17 = new ImmediatelyFollowedBy(new Atom(EventType.EVENT_A), new Atom(EventType.EVENT_B));

        Collection<Event> events = Generator.generateKleeneNotContiguous();
        Detector detector = new Detector(r16);
        detector.detect(events);
    }
}
