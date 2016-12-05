package fr.labri.patterndetector.runtime.expressions.predicates;

import fr.labri.patterndetector.runtime.expressions.IField;
import fr.labri.patterndetector.lang.Builtin.InvalidBuiltin;

/**
 * Created by morandat on 05/12/2016.
 */
public interface Factory<K> {
    K instanciate(IField[] fields) throws InvalidBuiltin;
}
