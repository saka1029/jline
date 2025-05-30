package saka1029.jline;

import org.jline.reader.Parser;
import org.jline.reader.impl.DefaultParser;
import java.io.IOException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Main {
    public static void main(String[] args) throws IOException {
        // JLine terminal の準備
        Terminal terminal = TerminalBuilder.builder().system(true).build();

        // Parser の準備
        Parser p = (Parser) new DefaultParser();
        LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).parser(p).build();

        // REPL
        while (true) {
            String line = lineReader.readLine("input > ");

            String[] exitCommands = {":exit"};
            for (String cmd : exitCommands) {
                if (line.contains(cmd)) {
                    return;
                }
            }

            System.out.println(line);
        }
    }
}
