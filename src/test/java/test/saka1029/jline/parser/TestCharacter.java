package test.saka1029.jline.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestCharacter {

    @Test
    public void testIsLetter() {
        assertTrue(Character.isLetter('a'));
        assertFalse(Character.isLetter(','));
        assertFalse(Character.isLetter('3'));
        assertTrue(Character.isLetter('漢'));
        assertTrue(Character.isLetter(Character.codePointAt("𩸽", 0)));
        assertFalse(Character.isLetter("𩸽".charAt(0)));
        assertFalse(Character.isLetter('０'));
        assertFalse(Character.isLetter('Ⅴ'));  // diff!!
    }

    @Test
    public void testIsAlphabetic() {
        assertTrue(Character.isAlphabetic('a'));
        assertFalse(Character.isAlphabetic(','));
        assertFalse(Character.isAlphabetic('3'));
        assertTrue(Character.isAlphabetic('漢'));
        assertTrue(Character.isAlphabetic(Character.codePointAt("𩸽", 0)));
        assertFalse(Character.isAlphabetic("𩸽".charAt(0)));
        assertFalse(Character.isAlphabetic('０'));
        assertTrue(Character.isAlphabetic('Ⅴ'));  // diff!!
    }

    @Test
    public void testIsDigit() {
        assertFalse(Character.isDigit('a'));
        assertFalse(Character.isDigit(','));
        assertTrue(Character.isDigit('3'));
        assertFalse(Character.isDigit('漢'));
        assertFalse(Character.isDigit(Character.codePointAt("𩸽", 0)));
        assertFalse(Character.isDigit("𩸽".charAt(0)));
        assertTrue(Character.isDigit('０'));
        assertFalse(Character.isDigit('Ⅴ'));
    }
}
