package saka1029.jline;

import java.io.IOException;
import org.jline.reader.LineReader;
import org.jline.reader.Parser;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.ParsedLine;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class CalcMain {

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
            lineReader.readLine("input > ");
            ParsedLine pl = lineReader.getParsedLine();
            System.out.println("{tokens=" + pl.words() + " line=" + pl.line() + "}");
            String line = pl.line();

            String[] exitCommands = {"exit"};
            for (String cmd : exitCommands) {
                if (line.contains(cmd)) {
                    return;
                }
            }
            System.out.println(line);
        }
    }
}
