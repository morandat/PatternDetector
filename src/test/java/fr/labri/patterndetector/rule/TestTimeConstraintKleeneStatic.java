package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.runtime.AutomatonRunnerType;
import fr.labri.patterndetector.runtime.Event;
import fr.labri.patterndetector.runtime.expressions.Constant;
import fr.labri.patterndetector.runtime.expressions.FieldAccess;
import fr.labri.patterndetector.runtime.expressions.builtins.Add;
import fr.labri.patterndetector.runtime.expressions.builtins.GreaterThan;
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
                                " Detect scenario : k+ -> end # end.t - k[0].t <= 10 ",
                                new FollowedBy(
                                        new Kleene("k"),
                                        new Atom("end")
                                                .addPredicate(new GreaterThan(
                                                        new Add(FieldAccess.byStaticIndex(0, 0).timestamp(),
                                                                Constant.from(10)),
                                                        FieldAccess.current().timestamp()))),
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
