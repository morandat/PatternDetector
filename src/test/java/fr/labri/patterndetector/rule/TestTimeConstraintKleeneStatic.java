package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.AutomatonRunnerType;
import fr.labri.patterndetector.runtime.Event;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

@RunWith(Parameterized.class)
public class TestTimeConstraintKleeneStatic extends AbstractTestDetection {

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
                                " Detect scenario : k+ -> end # end.t - k[0].t < 10 ",
                                new FollowedBy(
                                        new Kleene("k"),
                                        new Atom("end")
                                                .addPredicate(new LongPredicateArity2(
                                                        new TimeFieldKleeneStaticIndex("0", 0),
                                                        new TimeFieldAtom("1"),
                                                        (x, y) -> y.getValue() - x.getValue() < 10))),
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
