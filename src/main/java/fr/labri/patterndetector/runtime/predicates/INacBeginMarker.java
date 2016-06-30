package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.rule.IRule;

/**
 * Created by william.braik on 28/06/2016.
 */
public interface INacBeginMarker {

    IRule getNacRule();

    String getNacId();
}
