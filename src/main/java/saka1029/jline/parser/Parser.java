package saka1029.jline.parser;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Parser {

    static final int SCALE = 50;
    static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    int input[], index, ch;

    int get() {
        return ch = index < input.length ? input[index++] : -1;
    }

    void spaces() {
        while (Character.isWhitespace(ch))
            get();
    }

    boolean eat(String expect) {
        return eat(expect.codePoints().toArray());
    }

    boolean eat(int... expects) {
        spaces();
        int save = index;
        for (int e : expects)
            if (ch == e) {
                get();
            } else {
                index = save - 1;
                get();
                return false;
            }
        return true;
    }

    RuntimeException error(String format, Object... args) {
        return new RuntimeException(format.formatted(args));
    }

    static boolean isDigit(int ch) {
        return Character.isDigit(ch);
        // return switch (ch) {
        //     case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> true;
        //     default -> false;
        // };
    }

    static String characterString(int ch) {
        return ch == -1 ? "EOF" : "'%c'".formatted(ch);
    }

    StringBuilder sb = new StringBuilder();

    void appendGet(int ch) {
        sb.appendCodePoint(ch);
        get();
    }

    BigDecimal paren() {
        BigDecimal result = expression();
        if (!eat(')'))
            throw error("')' expected");
        return result;
    }

    BigDecimal number() {
        sb.setLength(0);
        while (isDigit(ch))
            appendGet(ch);
        if (ch == '.') {
            appendGet(ch);
            while (isDigit(ch))
                appendGet(ch);
        }
        if (ch == 'e' || ch == 'E') {
            appendGet(ch);
            if (ch == '+' || ch == '-')
                appendGet(ch);
            if (!isDigit(ch))
                throw error("Digit expected but '%c'", ch);
            while (isDigit(ch))
                appendGet(ch);
        }
        return new BigDecimal(sb.toString());
    }

    BigDecimal factor() {
        if (eat('('))
            return paren();
        else if (isDigit(ch))
            return number();
        else
            throw error("Unexpected char %s", characterString(ch));
    }

    BigDecimal term() {
        BigDecimal result = factor();
        while (true)
            if (eat('*'))
                result = result.multiply(term());
            else if (eat('/'))
                result = result.divide(term(), SCALE, ROUNDING);
            else
                break;
        return result;
    }

    BigDecimal expression() {
        BigDecimal result = term();
        while (true)
            if (eat('+'))
                result = result.add(term());
            else if (eat('-'))
                result = result.subtract(term());
            else
                break;
        return result;
    }

    public BigDecimal parse(String input) {
        this.input = input.codePoints().toArray();
        index = 0;
        get();
        return expression();
    }
}
