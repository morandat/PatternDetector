package fr.labri.patterndetector.rule;

import fr.labri.patterndetector.executor.Detector;
import fr.labri.patterndetector.executor.ForkAutomatonRunner;
import fr.labri.patterndetector.executor.RuleManager;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * Created by will on 26/10/15.
 */
public class ForkStatesTest {

    @Rule
    public TestName _testName = new TestName();

    @Test
    public void forkStatesTest() {
        IRule r = new FollowedBy("q", new FollowedBy(new Kleene("f"), "a"));

        RuleManager ruleManager = new RuleManager();
        Detector detector = new Detector(ruleManager);

        String ruleName = ruleManager.addRule(r, ForkAutomatonRunner.class);
        detector.detect(TestSearchDetection.searchScenario());

        System.out.println(ruleManager.getAutomaton(ruleName));
        ruleManager.getRunner(ruleName).getCurrentStates().forEach(System.out::println);
    }
}
