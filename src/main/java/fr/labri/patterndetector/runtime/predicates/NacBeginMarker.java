package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.rule.IRule;

/**
 * Created by william.braik on 28/06/2016.
 */
public class NacBeginMarker implements INacBeginMarker {

    private IRule _nacRule;
    private String _nacId;

    public NacBeginMarker(IRule nacRule, String nacId) {
        _nacRule = nacRule;
        _nacId = nacId;
    }

    @Override
    public String getNacId() {
        return _nacId;
    }

    @Override
    public IRule getNacRule() {
        return _nacRule;
    }
}
