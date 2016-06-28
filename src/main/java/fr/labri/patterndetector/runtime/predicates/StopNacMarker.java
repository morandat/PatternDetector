package fr.labri.patterndetector.runtime.predicates;

/**
 * Created by william.braik on 28/06/2016.
 */
public class StopNacMarker implements IStopNacMarker {

    private String _nacId;

    public StopNacMarker(String nacId) {
        _nacId = nacId;
    }

    @Override
    public String getNacId() {
        return _nacId;
    }
}
