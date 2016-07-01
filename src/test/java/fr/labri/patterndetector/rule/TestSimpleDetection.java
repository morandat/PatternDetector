package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.AutomatonRunnerType;
import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.Event;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;


@RunWith(Parameterized.class)
public class TestSimpleDetection extends AbstractTestDetection {

    public Stream<? extends Event> generate() {
        return Arrays.asList(
                new Event("b", 1),
                new Event("a", 2).setData("x", 10),
                new Event("c", 3)
        ).stream();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection patterns() {
        return Arrays.asList(
                new Object[][]{
                        {
                                "Detect A",
                                new Atom("a"),
                                Arrays.asList(
                                        new Event("a", 2)),
                                AutomatonRunnerType.Deterministic
                        }
                });
    }
}
