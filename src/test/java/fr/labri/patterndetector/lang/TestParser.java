
package fr.labri.patterndetector.lang;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import xtc.parser.Result;
import xtc.util.Pair;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import fr.labri.patterndetector.lang.AST.*;
import static fr.labri.patterndetector.lang.AST.*;

public class TestParser {

    Reader readerFromResource(String name) {
        return new InputStreamReader(getClass().getClassLoader().getResourceAsStream(name));
    }

    Parser newParserFor(String resource) {
        return new Parser(readerFromResource(resource), resource);
    }

    @Test
    public void testInitializeEmptyPaser() throws Exception {
        Parser p = new Parser(new StringReader(""), "empty");
        Result result = p.pscript(0);
        assertThat(result, is(notNullValue()));
        assertThat(result.semanticValue(), is(equalTo(Pair.EMPTY)));
    }

    Pair<Rule> simple = new Pair<>(
        new Rule("rule").
                appendPattern(newSimplePattern("foo")).
                appendPattern(newSimplePattern("bar")).
                appendPattern(newSimplePattern("baz"))
    );

    @Test
    public void testSimple() throws Exception {
        Parser p = newParserFor("simple.txt");
        Result result = p.pscript(0);
        assertThat(result, is(notNullValue()));
        assertThat(result.hasValue(), is(true));
        assertThat(result.semanticValue(), is(equalTo(simple)));
    }
}
