package saka1029.jline.calc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jline.reader.EOFError;
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
public class Main {

    static class Context {
        Map<String, Integer> variables = new HashMap<>();
    }

    interface Expression {
        int eval(Context c);
    }

    static class ExpressionParser implements Parser {
 
        static boolean matchParens(String line) {
            int nest = 0;
            for (int i = 0, max = line.length(); i < max; ++i)
                switch (line.charAt(i)) {
                    case '(':
                        ++nest;
                        break;
                    case ')':
                        if (nest <= 0)
                            return false;
                        --nest;
                        break;
                }
            return nest == 0;
        }        

        int[] input;
        int ch, index = 0;
        Expression expression;

        EOFError error(String message, Object... args) {
            return new EOFError(0, 0, message.formatted(args));
        }

        int get() {
            return ch = index >= input.length ? -1 : input[index++];
        }

        void spaces() {
            while (Character.isWhitespace(ch))
                get();
        }

        Expression number() {
            int prev = 0;
            for (; Character.isDigit(ch); get())
                prev = prev * 10 + Character.digit(ch, 10);
            int value = prev;
            return c -> value;
        }

        Expression factor() {
            spaces();
            if (ch == -1) throw error("No factor");
            switch (ch) {
                case '(':
                    Expression e = expression();
                    spaces();
                    if (get() != ')')
                        throw error("')' expected");
                    return e;
                default:
                    if (Character.isDigit(ch))
                        return number();
                    else
                        throw error("Unknown char '%c'", ch);
            }
        }

        Expression term() {
            spaces();
            if (ch == -1) throw error("No term");
            Expression e = factor();
            L: while (true) {
                Expression ee = e;
                switch (ch) {
                    case '*': get(); e = c -> ee.eval(c) * factor().eval(c); break;
                    case '/': get(); e = c -> ee.eval(c) / factor().eval(c); break;
                    case '%': get(); e = c -> ee.eval(c) % factor().eval(c); break;
                    default: break L;
                }
            }
            return e;
        }

        Expression expression() {
            spaces();
            if (ch == -1) throw error("No input");
            boolean negative = false;
            if (ch == '-') {
                get();
                spaces();
                negative = true;
            }
            Expression e = term();
            if (negative) {
                Expression ee = e;
                e = c -> -ee.eval(c);
            }
            L: while (true) {
                Expression ee = e;
                switch (ch) {
                    case '+': get(); e = c -> ee.eval(c) + term().eval(c); break;
                    case '-': get(); e = c -> ee.eval(c) - term().eval(c); break;
                    default: break L;
                }
            }
            return e;
        }

        @Override
        public ParsedLine parse(String line, int cursor, ParseContext context) throws SyntaxError {
            // System.out.printf("{parser(\"%s\")}", line);
            this.input = line.codePoints().toArray();
            get();
            this.expression = expression();
            // if (!matchParens(line))
            //     throw new EOFError(0, 0, line);
            return null;
        }

    }

    public static void main(String[] args) throws IOException {
        // JLine terminal の準備
        Terminal terminal = TerminalBuilder.builder()
            .system(true)
            .build();

        // Parser の準備
        ExpressionParser parser = new ExpressionParser();
        LineReader lineReader = LineReaderBuilder.builder()
            .terminal(terminal)
            .parser(parser)
            .build();
        lineReader.setVariable(LineReader.SECONDARY_PROMPT_PATTERN, "      >   ");;

        // REPL
        Context context = new Context();
        while (true) {
            try {
                String line = lineReader.readLine("input > ");
                if (line.equals(".exit"))
                    break;
                System.out.println(parser.expression.eval(context));
            } catch (EndOfFileException e) {
                break;
            }

        }
    }
}
