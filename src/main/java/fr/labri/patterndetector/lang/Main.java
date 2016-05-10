package fr.labri.patterndetector.lang;

import xtc.util.Pair;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import fr.labri.patterndetector.lang.AST.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Reader reader = args.length > 0 ? new FileReader(args[0]) : new InputStreamReader(System.in);
        Parser parser = new Parser(reader, "somefile");
        xtc.parser.Result result = parser.pscript(0);
        Pair<Rule> rules = result.semanticValue();
        for (Rule r: rules)
            System.out.println(r);
    }
}
