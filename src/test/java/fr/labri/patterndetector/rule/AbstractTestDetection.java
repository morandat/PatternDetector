package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.*;
import org.junit.*;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

abstract public class AbstractTestDetection {

    @Parameterized.Parameter(0)
    public String _testName;
    @Parameterized.Parameter(1)
    public IRule _testRule;
    @Parameterized.Parameter(2)
    public Collection<Event> _expectedPattern;
    @Parameterized.Parameter(3)
    public AutomatonRunnerType _runnerType;

    public abstract Stream<? extends Event> generate();

    @Test
    public void patternDetectionTest() {
        RuleManager ruleManager = new RuleManager();
        Detector detector = new Detector(ruleManager);
        IAutomatonRunner runner = ruleManager.addRule(_testRule, _runnerType);
        final AtomicInteger found = new AtomicInteger();
        runner.registerPatternObserver(
                (Collection<Event> pattern) -> {
                    found.incrementAndGet();
                    assertThat(pattern, is(equalTo(_expectedPattern)));
                });

        detector.detect(generate());
        assertThat("Should find all events", found.get(), is(equalTo(_expectedPattern.isEmpty() ? 0 : 1)));
    }
}
