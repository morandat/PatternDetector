package generators;

import fr.labri.patterndetector.executor.IEvent;

import java.util.stream.Stream;

/**
 * Interface for event stream generators
 */
public interface IGenerator {

    Stream<IEvent> generate();
}
