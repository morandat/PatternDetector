/**
 * Created by William Braik on 6/22/2015.
 */
public class Main {

    public static void main(String[] args) {

        Event[] events = {
                new Event(EventType.EVENT_A, 0, 0),
                new Event(EventType.EVENT_B, 1, 0)
        };

        Detector detector = new Detector(new Spec());
        boolean res = detector.detect(events);

        System.out.println(res);
    }
}
