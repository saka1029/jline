package saka1029.jline.calc;

import java.io.IOException;
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

    static EOFError error(String message, Object... args) {
        return new EOFError(0, 0, message.formatted(args));
    }

    static class ExpressionParser implements Parser {
 
        int input[], ch, index;
        long expression;

        int get() {
            return ch = index < input.length ? input[index++] : -1;
        }

        void spaces() {
            while (Character.isWhitespace(ch))
                get();
        }

        boolean eat(int expected) {
            spaces();
            if (ch == expected) {
                get();
                return true;
            }
            return false;
        }

        long factor() {
            if (eat(-1)) {
                throw error("Unexpected end");
            } else if (eat('(')) {
                long value = expression();
                if (!eat(')'))
                    throw error("')' expected");
                return value;
            } else if (Character.isDigit(ch)) {
                long value = 0;
                do {
                    value = value * 10 + Character.digit(ch, 10);
                    get();
                } while (Character.isDigit(ch));
                return value;
            } else
                throw new SyntaxError(0, 0, "Unknown char '%c'".formatted(ch));
        }

        long term() {
            long e = factor();
            while (true)
                if (eat('*'))
                    e *= factor();
                else if (eat('/'))
                    e /= factor();
                else if (eat('%'))
                    e %= factor();
                else
                    break;
            return e;
        }

        long expression() {
            boolean negative = false;
            if (eat('-'))
                negative = true;
            long e = term();
            if (negative)
                e = -e;
            while (true)
                if (eat('+'))
                    e += term();
                else if (eat('-'))
                    e -= term();
                else
                    break;
            return e;
        }

        /**
         * SYNTAX:
         * <ul>
         * <li>expression = [ '-' ] term { ( '+' | '-' ) term }</li>
         * <li>term       = factor { ( '*' | '/' | '%' ) factor }</li>
         * <li>factor     = '(' expression ')' | number</li>
         * <li>number     = { DIGIT }</li>
         * <li>DIGIT      = '0' ... '9'</li>
         * </ul>
         */
        @Override
        public ParsedLine parse(String line, int cursor, ParseContext context) throws SyntaxError {
            this.input = line.codePoints().toArray();
            this.index = 0;
            get();
            try {
                this.expression = expression();
                System.out.println();
                System.out.print(this.expression);
            } catch (EOFError e) {
                throw e;
            } catch (SyntaxError s) {
                System.out.println();
                System.out.print(s.getMessage());
                throw s;
            }
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
        while (true) {
            try {
                lineReader.readLine("input > ");
                // System.out.println(parser.expression);
            } catch (EndOfFileException e) {
                break;
            }

        }
    }
}
