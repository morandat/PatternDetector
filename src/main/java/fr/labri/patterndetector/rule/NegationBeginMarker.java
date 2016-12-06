package fr.labri.patterndetector.rule;

import java.io.Serializable;

/**
 * Created by william.braik on 28/06/2016.
 */
public class NegationBeginMarker implements INegationBeginMarker, Serializable{

    private IRule _negationRule;
    private String _negationId;

    public NegationBeginMarker(IRule negationRule, String negationId) {
        _negationRule = negationRule;
        _negationId = negationId;
    }

    @Override
    public String getNegationId() {
        return _negationId;
    }

    @Override
    public IRule getNegationRule() {
        return _negationRule;
    }
}
