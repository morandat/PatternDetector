import java.util.ArrayList;

/**
 * Created by William Braik on 6/22/2015.
 */
public class Main {

    public static void main(String[] args) {

        ArrayList<Event> eventStream = Generator.generateStream();

        Rule rule = new AlwaysFollowedBy(EventType.EVENT_A, EventType.EVENT_B);

        Detector detector = new Detector(rule);
        boolean res = detector.detect(eventStream);

        System.out.println(res);
    }
}
