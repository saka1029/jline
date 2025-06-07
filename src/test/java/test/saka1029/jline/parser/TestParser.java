package test.saka1029.jline.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.math.BigDecimal;
import org.junit.Test;
import saka1029.jline.parser.Parser;

public class TestParser {

    @Test
    public void testParse() {
        Parser parser = new Parser();
        assertEquals(BigDecimal.valueOf(3), parser.parse("1 + 2"));
        assertEquals(BigDecimal.valueOf(235), parser.parse("１ + ２3４"));
    }

    @Test
    public void testParseError() {
        Parser parser = new Parser();
        try {
            parser.parse("a");
            fail();
        } catch (RuntimeException e) {
            assertEquals("Unexpected char 'a'", e.getMessage());
        }
    }
}
