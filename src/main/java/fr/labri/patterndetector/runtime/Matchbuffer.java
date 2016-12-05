package fr.labri.patterndetector.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by morandat on 02/12/2016.
 */
public class Matchbuffer {
    final private List<Event> _slots[];

    public Matchbuffer(int fieldsCount) {
        this._slots = new List[fieldsCount];
    }

    public List<Event> get(int position) {
        return _slots[position];
    }

    void put(int position, Event e) {
        _slots[position] = Arrays.asList(e);
    }

    public boolean hasEvent(int fieldPosition, int eventPosition) {
        List<Event> slot = _slots[fieldPosition];
        return slot != null && slot.size() >= eventPosition;
    }

    void append(int position, Event e) {
        List<Event> slot = _slots[position];
        if (slot == null)
            _slots[position] = slot = new ArrayList<>();
        slot.add(e);
    }

    public Stream<Event> asStream() {
        ArrayList<Event> matchBuffer = new ArrayList<>();
        for (List<Event> slot : _slots) {
            if (slot != null)
                matchBuffer.addAll(slot);
        }

        return matchBuffer.stream()
                .sorted((e1, e2) -> new Long(e1.getTimestamp()).compareTo(e2.getTimestamp())); // make sure it's sorted by timestamp
    }

    public void clear() {
        for (int i = 0; i < _slots.length; i++)
            _slots[i] = null;
    }

    public Matchbuffer duplicate() {
        Matchbuffer m = new Matchbuffer(_slots.length);
        for (int i = 0; i < _slots.length; i++) {
            List<Event> slot = _slots[i];
            if (slot != null) {
                m._slots[i] = new ArrayList<>(slot.size());
                m._slots[i].addAll(slot);
            }
        }
        return m;
    }
}
