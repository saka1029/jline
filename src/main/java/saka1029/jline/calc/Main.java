package saka1029.jline.calc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    static EOFError error(String message, Object... args) {
        return new EOFError(0, 0, message.formatted(args));
    }

    static class Scanner {
        int input[], ch, index;

        int get() {
            return ch = index < input.length ? input[index++] : -1;
        }

        StringBuilder sb = new StringBuilder();

        List<String> scan(String input) {
            this.input = input.codePoints().toArray();
            this.index = 0;
            get();
            List<String> list = new ArrayList<>();
            L: while (true) {
                while (Character.isWhitespace(ch))
                    get();
                sb.setLength(0);
                switch (ch) {
                    case -1:
                        break L;
                    case '(': case ')':
                    case '+': case '-':
                    case '*': case '/': case '%':
                        list.add(Character.toString(ch));
                        get();
                        break;
                    default:
                        if (Character.isDigit(ch)) {
                            for (; Character.isDigit(ch); get())
                                sb.appendCodePoint(ch);
                            list.add(sb.toString());
                        } else if (Character.isAlphabetic(ch)) {
                            for (; Character.isAlphabetic(ch) || Character.isDigit(ch); get())
                                sb.appendCodePoint(ch);
                            list.add(sb.toString());
                        } else
                            throw error("Unknown char '%c'", ch);
                        break;
                }
            }
            return list;
        }

    }

    static class ExpressionParser implements Parser {
 
        Scanner scanner = new Scanner();
        List<String> tokens;
        String token;
        int index = 0;
        Expression expression;

        String get() {
            return token = index >= tokens.size() ? null : tokens.get(index++);
        }

        Expression factor() {
            if (token == null) throw error("No factor");
            switch (token) {
                case "(":
                    get(); // skip "("
                    Expression e = expression();
                    if (token == null || !token.equals(")"))
                        throw error("')' expected");
                    return e;
                default:
                    if (Character.isDigit(token.charAt(0))) {
                        int i = Integer.parseInt(token);
                        get(); // skip NUMBER
                        return c -> i;
                    } else if (Character.isAlphabetic(token.charAt(0))) {
                        String v = token;
                        get(); // skip VARIABLE
                        return c -> c.variables.get(v);
                    } else
                        throw error("Unknown token '%s'", token);
            }
        }

        Expression term() {
            Expression e = factor();
            L: while (true) {
                Expression l = e;
                if (token == null) break L;
                Expression r;
                switch (token) {
                    case "*":
                        get();
                        r = factor();
                        e = c -> l.eval(c) * r.eval(c);
                        break;
                    case "/":
                        get();
                        r = factor();
                        e = c -> l.eval(c) / r.eval(c);
                        break;
                    case "%":
                        get();
                        r = factor();
                        e = c -> l.eval(c) % r.eval(c);
                        break;
                    default: break L;
                }
            }
            return e;
        }

        Expression expression() {
            if (token == null) throw error("No expression");
            boolean negative = false;
            if (token.equals("-")) {
                get();
                negative = true;
            }
            Expression e = term();
            if (negative) {
                Expression ee = e;
                e = c -> -ee.eval(c);
            }
            L: while (true) {
                Expression ee = e;
                if (token == null) break L;
                Expression r;
                switch (token) {
                    case "+":
                        get();
                        r = term();
                        e = c -> ee.eval(c) + r.eval(c);
                        break;
                    case "-":
                        get();
                        r = term();
                        e = c -> ee.eval(c) - r.eval(c);
                        break;
                    default: break L;
                }
            }
            return e;
        }

        @Override
        public ParsedLine parse(String line, int cursor, ParseContext context) throws SyntaxError {
            // System.out.printf("{parser(\"%s\")}", line);
            this.tokens = scanner.scan(line);
            this.index = 0;
            get();
            System.out.println("tokens=" + this.tokens);
            try {
                this.expression = expression();
            } catch (EOFError e) {
                System.out.println(e);
                throw e;
            }
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
