package fr.labri.patterndetector.runtime.expressions;

import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.Matchbuffer;
import fr.labri.patterndetector.runtime.UnknownFieldException;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by morandat on 06/12/2016.
 */
public interface FieldAccess {
    IField named(String name);
    IField timestamp();

     static FieldAccess current() {
        return new FieldAccess() {

            @Override
            public IField named(String name) {
                return new IField() {
                    @Override
                    public Optional<IValue<?>> fetch(Matchbuffer matchbuffer, Event currentEvent) throws UnknownFieldException {
                        return Optional.of(getFieldValue(currentEvent, name));
                    }
                };
            }

            @Override
            public IField timestamp() {
                return new IField() {
                    @Override
                    public Optional<IValue<?>> fetch(Matchbuffer matchbuffer, Event currentEvent) throws UnknownFieldException {
                        return Optional.of(IValue.LongValue.from(currentEvent.getTimestamp()));
                    }
                };
            }
        };
    }

    static FieldAccess byPosition(int position) {
        return new FieldAccess() {
            @Override
            public IField named(String name) {
                return new IField() {
                    @Override
                    public Optional<IValue<?>> fetch(Matchbuffer matchbuffer, Event currentEvent) throws UnknownFieldException {
                        assert position < matchbuffer.size();
                        return Optional.of(getFieldValue(matchbuffer.get(position).get(0), name));
                    }
                };
            }

            @Override
            public IField timestamp() {
                return new IField() {
                    @Override
                    public Optional<IValue<?>> fetch(Matchbuffer matchbuffer, Event currentEvent) throws UnknownFieldException {
                        assert position < matchbuffer.size();
                        return Optional.of(IValue.LongValue.from(matchbuffer.get(position).get(0).getTimestamp()));
                    }
                };
            }
        };
    }
    static FieldAccess byStaticIndex(int position, int index) {
        return new FieldAccess() {
            @Override
            public IField named(String name) {
                return new StaticIndex(position, index) {
                    @Override
                    IValue<?> getValue(Event e) throws UnknownFieldException {
                        return getFieldValue(e, name);
                    }
                };
            }

            @Override
            public IField timestamp() {
                return new StaticIndex(position, index) {
                    @Override
                    IValue<?> getValue(Event e) throws UnknownFieldException {
                        return IValue.LongValue.from(e.getTimestamp());
                    }
                };
            }
        };
    }

    static FieldAccess byDynamicIndex(int position, Function<List<Event>, Integer> indexFunction) {
        return new FieldAccess() {
            @Override
            public IField named(String name) {
                return new DynamicIndex(position, indexFunction) {
                    @Override
                    IValue<?> getValue(Event e) throws UnknownFieldException {
                        return getFieldValue(e, name);
                    }
                };
            }

            @Override
            public IField timestamp() {
                return new DynamicIndex(position, indexFunction) {
                    @Override
                    IValue<?> getValue(Event e) throws UnknownFieldException {
                        return IValue.LongValue.from(e.getTimestamp());
                    }
                };
            }
        };
    }

    abstract class FieldKleene implements IField {

        private final int _fieldPosition;

        public FieldKleene(int fieldPosition) {
            _fieldPosition = fieldPosition;
        }

        abstract int computeIndex(List<Event> events);

        abstract IValue<?> getValue(Event e) throws UnknownFieldException;

        @Override
        public Optional<IValue<?>> fetch(Matchbuffer matchbuffer, Event currentEvent) throws UnknownFieldException {
            assert _fieldPosition < matchbuffer.size();
            List<Event> events = matchbuffer.get(_fieldPosition);
            if (events == null)
                return Optional.empty();
            int index = computeIndex(events);
            if (index >= 0 && index < events.size())
                return Optional.of(getValue(events.get(index)));
            return Optional.empty();
        }
    }
    /**
     * This class fetch an item from a kleene field using a static index.
     *
     * Index can be positive, which directly match to the same index in the sequence.
     * It can also be negative counting indexes backwards (-1 is the last).
     */
    abstract class StaticIndex extends FieldKleene {

        protected int _index;

        public StaticIndex(int fieldPosition, int index) {
            super(fieldPosition);
            _index = index;
        }

        int computeIndex(List<Event> events) {
            return _index >= 0 ? _index : events.size() + _index;
        }
    }

    /**
     * Created by wbraik on 08/06/16.
     */
    abstract class DynamicIndex extends FieldKleene {

        private final Function<List<Event>, Integer> _indexFunc;

        public DynamicIndex(int fieldPosition, Function<List<Event>, Integer> computeIndex) {
            super(fieldPosition);
            _indexFunc = computeIndex;
        }

        @Override
        int computeIndex(List<Event> events) {
            return _indexFunc.apply(events);
        }
    }
}
