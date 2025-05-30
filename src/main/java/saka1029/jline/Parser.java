package saka1029.jline;

import java.util.ArrayDeque;
import java.util.Deque;

public class Parser {

    public static boolean matchParens(String s) throws ParseException {
        Deque<Integer> stack = new ArrayDeque<>();
        for (int c : (Iterable<Integer>)() -> s.codePoints().iterator()) {
            switch (c) {
                case '(': stack.push((int)')'); break;
                case '{': stack.push((int)'}'); break;
                case ')':
                case '}':
                    if (stack.size() <= 0)
                        throw new ParseException("Too many '%c'", c);
                    int prev = stack.pop();
                    if (c != prev)
                        throw new ParseException("Expected '%c' but '%c'", prev, c);
                    break;
            }
        }
        return stack.size() == 0;
    }

}
