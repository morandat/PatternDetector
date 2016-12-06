package fr.labri.patterndetector.rule;

import java.io.Serializable;

/**
 * Created by william.braik on 28/06/2016.
 */
public class NegationEndMarker implements INegationEndMarker, Serializable {

    private String _negationId;

    public NegationEndMarker(String negationId) {
        _negationId = negationId;
    }

    @Override
    public String getNegationId() {
        return _negationId;
    }
}
