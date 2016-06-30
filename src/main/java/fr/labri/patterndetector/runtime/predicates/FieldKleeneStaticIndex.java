package fr.labri.patterndetector.runtime.predicates;

import fr.labri.patterndetector.runtime.IEvent;
import fr.labri.patterndetector.types.IValue;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by wbraik on 08/06/16.
 */
public class FieldKleeneStaticIndex extends AbstractField {

    protected int _index;

    public FieldKleeneStaticIndex(String patternId, String fieldName, int index) {
        super(patternId, fieldName);
        _index = index;
    }

    public int getIndex() {
        return _index;
    }

    @Override
    public boolean isResolvable(ArrayList<IEvent> matchBuffer, String currentMatchBufferKey, IEvent currentEvent) {
        if (_patternId.equals(currentMatchBufferKey)) { // currently processed kleene
            if (matchBuffer == null) { // current event is first event of the kleene
                if (_index != 0)
                    return false;
            } else { // already at least one event in the kleene
                int currentIndex = matchBuffer.size();
                if (_index > currentIndex)
                    return false;
            }
        } else if (_index >= matchBuffer.size()) { // previously processed kleene
            return false;
        }

        return true;
    }

    @Override
    public Optional<IValue<?>> resolve(ArrayList<IEvent> matchBuffer, String currentMatchBufferKey, IEvent currentEvent) {
        if (_patternId.equals(currentMatchBufferKey)) {
            if (matchBuffer == null) { // first event of the current kleene seq
                return Optional.ofNullable(currentEvent.getPayload().get(_fieldName));
            } else { // at least one event already in current kleene seq
                int currentIndex = matchBuffer.size(); // index of the currently processed event (not yet appended)

                if (_index == currentIndex) {
                    return Optional.ofNullable(currentEvent.getPayload().get(_fieldName));
                } else {
                    IEvent event = matchBuffer.get(_index);
                    return Optional.ofNullable(event.getPayload().get(_fieldName));
                }
            }
        } else { // past kleene sequence
            if (_index >= matchBuffer.size()) {
                return Optional.empty();
            } else {
                IEvent event = matchBuffer.get(_index);
                return Optional.ofNullable(event.getPayload().get(_fieldName));
            }
        }
    }
}
