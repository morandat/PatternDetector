package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.rule.IRule;
import fr.labri.patterndetector.rule.visitors.RulesNamer;

/**
 * Created by william.braik on 28/06/2016.
 */
public class StartNacMarker implements IStartNacMarker {

    private IRule _nacRule;
    private String _nacId;

    public StartNacMarker(IRule nacRule, String nacId) {
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
