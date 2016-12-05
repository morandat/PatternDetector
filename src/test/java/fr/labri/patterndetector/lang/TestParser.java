package fr.labri.patterndetector.lang;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import xtc.parser.ParseError;
import xtc.parser.Result;
import xtc.util.Pair;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;

import fr.labri.patterndetector.lang.AST.*;
import static fr.labri.patterndetector.lang.AST.*;

@RunWith(Parameterized.class)
public class TestParser {

    @Parameterized.Parameter(value = 0)
    public String resource;
    @Parameterized.Parameter(value = 1)
    public Pair<Rule> expected;


    @Test
    public void testParsingFromRessource() throws Exception {
        Parser p = newParserFor(resource);
        assertThat(p.pscript(0), parseTo(expected));
    }

    final static Pair<Rule> simple = pairOf(
        new Rule("rule").
                appendPattern(newSimplePattern("foo")).
                appendPattern(newSimplePattern("bar")).
                appendPattern(newSimplePattern("baz"))
    );

    final static Pair<Rule> multiple = pairOf(
        new Rule("rule").
                appendPattern(newSimplePattern("foo")).
                appendPattern(newSimplePattern("bar")).
                appendPattern(newSimplePattern("baz")),
        new Rule("another").
                appendPattern(newKleene(newSimplePattern("foo"))).
                appendPattern(newKleene(newSimplePattern("bar"))).
                appendPattern(newKleene(newSimplePattern("baz"))),
        new Rule("yetanother").
                appendPattern(newSimplePattern("foo")).
                appendPattern(newKleene(newCompositePattern(new Pair<>(newSimplePattern("bar"))))).
                appendPattern(newSimplePattern("baz"))
    );

    @Parameterized.Parameters (name = "{index} {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                { "", Pair.empty() },
                { "simple.txt", simple },
                { "multiple.txt", multiple }
        });
    }

    @SafeVarargs
    public static <R> Pair<R> pairOf(R... objects) {
        if (objects.length == 0)
            return Pair.empty();
        Pair<R> pair = new Pair<>(objects[0]);
        for (int i = 1 ; i < objects.length ; i ++)
           pair.add(objects[i]);
        return pair;
    }

    Reader readerFromResource(String name) {
        if (name == null || "".equals(name))
            return new StringReader("");
        return new InputStreamReader(getClass().getClassLoader().getResourceAsStream(name));
    }

    Parser newParserFor(String resource) {
        return new Parser(readerFromResource(resource), resource);
    }

    Matcher<Result> parseTo(final Pair<Rule> rules) {
        return new TypeSafeMatcher<Result>() {
            @Override
            public void describeTo(Description description) {
                description.appendValue(rules);
            }

            @Override
            protected boolean matchesSafely(Result result) {
                return result.hasValue() &&
                        rules.equals(result.semanticValue());
            }

            @Override
            public void describeMismatchSafely(Result result, Description description) {
                if (result.hasValue()) {
                    description.appendValue(result.semanticValue());
                } else {
                    description.appendText("Parse error: ");
                    ParseError e = result.parseError();
                    description.appendValue(e.msg + " at " + e.index);
                }
            }
        };
    }
}
