package saka1029.jline;

import org.jline.reader.impl.DefaultParser;
import java.io.IOException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * @see https://takemikami.com/2019/0302-javacuijline.html
 */
public class Main {
    public static void main(String[] args) throws IOException {
        // JLine terminal の準備
        Terminal terminal = TerminalBuilder.builder()
            .system(true)
            .build();

        // Parser の準備 (単一行読み取り）
        // Parser p = (Parser) new DefaultParser();
        // LineReader lineReader = LineReaderBuilder.builder()
        //     .terminal(terminal)
        //     .parser(p)
        //     .build();
        
        // Parser の準備（複数行読み取り）
        // DefaultParser p = new DefaultParser();
        // p.setEofOnEscapedNewLine(true);
        // LineReader lineReader = LineReaderBuilder.builder()
        //     .terminal(terminal)
        //     .parser(p)
        //     .build();

        // Parser の準備 (セミコロンまで複数行読み取り)
        MultilineParser p = new MultilineParser();
        LineReader lineReader = LineReaderBuilder.builder()
            .terminal(terminal)
            .parser(p)
            .build();

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
