package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.AutomatonRunnerType;
import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.expressions.Constant;
import fr.labri.patterndetector.runtime.expressions.FieldCurrentTime;
import fr.labri.patterndetector.runtime.expressions.FieldKleeneStaticIndexTime;
import fr.labri.patterndetector.runtime.expressions.arithmetic.Add;
import fr.labri.patterndetector.runtime.expressions.predicates.Equal;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

@RunWith(Parameterized.class)
public class TestTimeConstraintKleeneDyn extends AbstractTestDetection {

    public Stream<? extends Event> generate() {
        return scenario();
    }

    public static Stream<? extends Event> scenario() {
        return Arrays.asList(
                new Event("k", 1),
                new Event("k", 2),
                new Event("k", 3),
                new Event("k", 4),
                new Event("end", 10)
        ).stream();
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection patterns() {
        return Arrays.asList(
                new Object[][]{
                        {
                                " Detect scenario : k+ -> end | k[i].t = k[i-1].t + 1 ",
                                new FollowedBy(
                                        new Kleene("k")
                                                .addPredicate(new Equal(
                                                        new FieldCurrentTime(),
                                                        new Add(new FieldKleeneStaticIndexTime(0, -1), new Constant(1)))),
                                        "end"),
                                Arrays.asList(
                                        new Event("k", 1),
                                        new Event("k", 2),
                                        new Event("k", 3),
                                        new Event("k", 4),
                                        new Event("end", 10)),
                                AutomatonRunnerType.Deterministic
                        }
                });
    }
}
