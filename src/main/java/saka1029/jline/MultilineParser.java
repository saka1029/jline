package saka1029.jline;

import org.jline.reader.EOFError;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.DefaultParser;

public class MultilineParser extends DefaultParser {

    @Override
    public ParsedLine parse(final String line, final int cursor, ParseContext context) {
        ParsedLine pl = super.parse(line, cursor, context);
        // System.err.println("parse: " + pl.line());

        // 読み取りが完了していなければEOFErrorをスローする。
        // こうすると読み取りが継続する。
        if (!line.trim().endsWith(";") && !line.startsWith(":")) {
            throw new EOFError(-1, -1, "Without semicolon", "");
        }
        return pl;
    }

}
