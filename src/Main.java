/**
 * Created by William Braik on 6/22/2015.
 */
public class Main {

    public static void main(String[] args) {

        Event[] events = Generator.generate();

        Detector detector = new Detector(new Spec());
        boolean res = detector.detect(events);

        System.out.println(res);
    }
}
