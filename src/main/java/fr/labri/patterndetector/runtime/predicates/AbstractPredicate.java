package fr.labri.patterndetector.runtime.predicates;

/**
 * Created by morandat on 05/12/2016.
 */
public abstract class AbstractPredicate implements IPredicate {
    private final IField _fields[];

    public AbstractPredicate(IField... fields) {
        _fields = fields;
    }

    public IField[] getFields() {
        return _fields;
    }
}
