package saka1029.jline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestParser {

    @Test
    public void testMatchParens() {
        assertTrue(Parser.matchParens("{ (a;)}"));
        assertFalse(Parser.matchParens("{ (a;)"));
        try {
            Parser.matchParens("())");
        } catch (ParseException e) {
            assertEquals("Too many ')'", e.getMessage());
        }
        try {
            Parser.matchParens("(}");
        } catch (ParseException e) {
            assertEquals("Expected ')' but '}'", e.getMessage());
        }
    }
}
