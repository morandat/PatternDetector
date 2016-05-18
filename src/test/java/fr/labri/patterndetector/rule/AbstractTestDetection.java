package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.executor.*;
import fr.labri.patterndetector.rule.*;
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
    public Collection<IEvent> _expectedPattern;

    public abstract Stream<? extends IEvent> generate();

    @Test
    public void patternDetectionTest() {
        RuleManager ruleManager = new RuleManager();
        Detector detector = new Detector(ruleManager);
        IAutomatonRunner runner = ruleManager.addRule(_testRule, AutomatonRunnerType.DFA);
        final AtomicInteger found = new AtomicInteger();
        runner.registerPatternObserver(
                (Collection<IEvent> pattern) -> {
                    found.incrementAndGet();
                    assertThat(pattern, is(equalTo(_expectedPattern)));
                });

        detector.detect(generate());
        assertThat(found.get(), is(equalTo(1)));
    }
}
