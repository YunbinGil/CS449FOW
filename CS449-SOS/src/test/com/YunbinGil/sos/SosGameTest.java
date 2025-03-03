package test.com.YunbinGil.sos;

import org.junit.jupiter.api.Test;
import com.YunbinGil.sos.SosGame;

import static org.junit.jupiter.api.Assertions.*;

class SosGameTest {
    @Test
    void testIsCellEmpty() {
        SosGame game = new SosGame(3);
        assertTrue(game.isCellEmpty(1, 1));
        game.placeLetter(1, 1, 'S');
        assertFalse(game.isCellEmpty(1, 1));
    }

    @Test
    void testPlaceLetter() {
        SosGame game = new SosGame(3);
        game.placeLetter(0, 0, 'S');
        assertEquals('S', game.getLetter(0, 0));
        game.placeLetter(0, 0, 'O'); // Should not overwrite
        assertEquals('S', game.getLetter(0, 0));
    }

    @Test
    void testResetBoard() {
        SosGame game = new SosGame(3);
        game.placeLetter(0, 0, 'S');
        game.placeLetter(1, 1, 'O');
        game.resetBoard();
        assertTrue(game.isCellEmpty(0, 0));
        assertTrue(game.isCellEmpty(1, 1));
    }

}
