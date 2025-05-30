package saka1029.jline;

public class ParseException extends RuntimeException {
    public ParseException(String format, Object... args) {
        super(format.formatted(args));
    }

}
