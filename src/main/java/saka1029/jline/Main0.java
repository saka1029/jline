package saka1029.jline;

import java.io.IOException;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.ParsedLine;
import org.jline.reader.Parser;
import org.jline.reader.SyntaxError;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * @see https://takemikami.com/2019/0302-javacuijline.html
 */
public class Main0 {
    static class ExpressionParser implements Parser {

        @Override
        public ParsedLine parse(String line, int cursor, ParseContext context) throws SyntaxError {
            System.out.printf("{ExpressionParser.parser(\"%s\") called}", line);
            return null;
        }

    }

    public static void main(String[] args) throws IOException {
        // JLine terminal の準備
        Terminal terminal = TerminalBuilder.builder()
            .system(true)
            .build();

        // Parser の準備
        Parser p = new ExpressionParser();
        LineReader lineReader = LineReaderBuilder.builder()
            .terminal(terminal)
            .parser(p)
            .build();

        // REPL
        while (true) {
            try {
                String line = lineReader.readLine("input > ");
                if (line.equals(".exit"))
                    break;
                System.out.println(line);
            } catch (EndOfFileException e) {
                break;
            }

        }
    }
}
