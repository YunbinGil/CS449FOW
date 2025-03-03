package test.com.YunbinGil.sos;

import com.YunbinGil.sos.SosGUI;
import com.YunbinGil.sos.SosGame;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SosGameTest {

    @Test
    void testIsCellEmpty() {
        SosGame game = new SosGame(3);
        assertTrue(game.isCellEmpty(1, 1), "Cell should be empty initially");
        game.placeLetter(1, 1, 'S');
        assertFalse(game.isCellEmpty(1, 1), "Cell should not be empty after placing a letter");
    }

    @Test
    void testPlaceLetter() {
        SosGame game = new SosGame(3);
        game.placeLetter(0, 0, 'S');
        assertEquals('S', game.getLetter(0, 0), "Cell should contain 'S'");
        game.placeLetter(0, 0, 'O'); // Should not overwrite
        assertEquals('S', game.getLetter(0, 0), "Cell should still contain 'S', overwriting is not allowed");
    }

    @Test
    void testResetBoard() {
        SosGame game = new SosGame(3);
        game.placeLetter(0, 0, 'S');
        game.placeLetter(1, 1, 'O');
        game.resetBoard();
        assertTrue(game.isCellEmpty(0, 0), "Board should be empty after reset");
        assertTrue(game.isCellEmpty(1, 1), "Board should be empty after reset");
    }

    @Test
    void testTurnSwitching() {
        SosGUI gui = new SosGUI();
        boolean initialTurn = gui.isBlueTurn();
        gui.placeLetter(0, 0);
        assertNotEquals(initialTurn, gui.isBlueTurn(), "Turn should switch after placing a letter");
    }

    @Test
    void testNewGameResetsTurn() {
        SosGUI gui = new SosGUI();
        gui.placeLetter(0, 0);
        gui.startNewGame();
        assertTrue(gui.isBlueTurn(), "New game should reset turn to Blue");
    }
}