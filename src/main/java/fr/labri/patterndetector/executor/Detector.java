package fr.labri.patterndetector.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Created by wbraik on 10/25/2015.
 * <p>
 * blabla
 */
public class Detector {

    private final Logger logger = LoggerFactory.getLogger(Detector.class);
    private RuleManager _ruleManager;

    public Detector(RuleManager ruleManager) {
        _ruleManager = ruleManager;
    }

    /**
     * Detect patterns in a stream of events.
     *
     * @param events The stream of events.
     */
    public void detect(Collection<IEvent> events) {
        logger.info("Searching patterns in stream : " + events);

        // Each event is forwarded to the rule manager(s) and then dispatched to the rules
        events.stream().forEach(_ruleManager::dispatchEvent);
    }
}
