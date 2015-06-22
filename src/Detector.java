/**
 * Created by William Braik on 6/22/2015.
 */

/** Parses a stream of events to detect patterns according to a given Spec **/
public class Detector {

    // For now the Detector only has a single Spec.
    Spec _spec;

    public Detector(Spec spec) {
        _spec = spec;
    }

    // For now this method returns whether any pattern (defined by the spec) was found within the input event sequence.
    public boolean detect(Event[] events) {

        return true;
    }

    // addSpec()
    // removeSpec()
}
