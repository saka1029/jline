package saka1029.jline;

import org.jline.reader.ParsedLine;
import org.jline.reader.SyntaxError;
import org.jline.reader.impl.DefaultParser;

public class ExpressionParser extends DefaultParser {
    String TOKEN_PAT = "[a-zA-Z][a-zA-Z0-9]*";
    ExpressionParser() {
        blockCommentDelims(new BlockCommentDelims("/*", "*/"));
        eofOnUnclosedBracket(Bracket.ROUND, Bracket.SQUARE);
        eofOnUnclosedQuote(true);
        eofOnEscapedNewLine(true);
        escapeChars(new char[] {'\\'});
        lineCommentDelims(new String[] {"#"});
        quoteChars(new char[] {'\"', '\''});
        // regexCommand(TOKEN_PAT);
        regexVariable(TOKEN_PAT);
    }

    @Override
    public ParsedLine parse(String line, int cursor, ParseContext context) throws SyntaxError {
        ParsedLine p = super.parse(line, cursor, context);
        // System.out.println("{line=" + line + " tokens=" + p.words() + " context=" + context + "}");
        return p;
    }
}
