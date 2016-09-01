package fr.labri.patterndetector.runtime;

import java.io.Serializable;

/**
 * Created by william.braik on 01/07/2016.
 */
public class AbstractRunContext implements IRunContext, Serializable {

    private static long currentContextId = 0;

    protected long _contextId;

    public AbstractRunContext() {
        _contextId = AbstractRunContext.currentContextId++;
    }

    @Override
    public long getContextId() {
        return _contextId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof AbstractRunContext)) return false;
        AbstractRunContext o = (AbstractRunContext) obj;
        return o.getContextId() == _contextId;
    }
}
