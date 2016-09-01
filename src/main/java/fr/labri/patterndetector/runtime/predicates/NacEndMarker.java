package fr.labri.patterndetector.runtime.predicates;

import java.io.Serializable;

/**
 * Created by william.braik on 28/06/2016.
 */
public class NacEndMarker implements INacEndMarker, Serializable {

    private String _nacId;

    public NacEndMarker(String nacId) {
        _nacId = nacId;
    }

    @Override
    public String getNacId() {
        return _nacId;
    }
}
