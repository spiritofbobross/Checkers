package tests;

import model.CheckersConfig;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// will be used to check legal move functions
public class TestCheckersConfig {
    CheckersConfig config = new CheckersConfig();

    @Test
    public void testIsValidMove() {
        assertTrue(config.isValidMove(5, 0, 4, 1));
        assertFalse(config.isValidMove(5, 0, 4, 0));
        assertFalse(config.isValidMove(6, 3, 4, 5));
        assertFalse(config.isValidMove(7, 2, 7, 3));
        assertTrue(config.isValidMove(2, 1, 3, 0));
        assertFalse(config.isValidMove(2, 1, 3, 1));
        assertFalse(config.isValidMove(1, 0, 3, 2));
        assertFalse(config.isValidMove(0, 5, 0, 6));
        assertFalse(config.isValidMove(3, 0, 4, 1));
        assertFalse(config.isValidMove(2, 1, 8, 5));
    }

    @Test
    public void testMove() {
        assertTrue(config.move(5, 4, 4, 3));
        assertEquals('-', config.getBoard()[5][4].getName());
        assertEquals('B', config.getBoard()[4][3].getName());
        assertFalse(config.move(4, 3, 5, 4));
        assertTrue(config.move(2, 3, 3, 2));
        assertEquals('-', config.getBoard()[2][3].getName());
        assertEquals('R', config.getBoard()[3][2].getName());
        assertTrue(config.move(5, 6, 4, 5));
        assertTrue(config.move(3, 2, 5, 4));
        assertEquals('-', config.getBoard()[3][2].getName());
        assertEquals('-', config.getBoard()[4][3].getName());
        assertEquals('R', config.getBoard()[5][4].getName());
    }
}
