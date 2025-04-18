package test.com.YunbinGil.sos;

import com.YunbinGil.sos.SimpleGame;
import com.YunbinGil.sos.SosGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SosGameTest {

    @Test
    public void testInitialBoardIsEmpty() {
        SosGame game = new SimpleGame(3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertTrue(game.isCellEmpty(i, j));
            }
        }
    }

    @Test
    public void testPlaceLetterChangesTurn() {
        SosGame game = new SimpleGame(3);

        boolean initialTurn = game.isBlueTurn();
        game.placeLetter(0, 0, 'S');
        assertNotEquals(initialTurn, game.isBlueTurn());
    }

    @Test
    public void testCheckDirectionReturnsTrue() {
        SosGame game = new SimpleGame(3);

        game.placeLetter(0, 0, 'S');
        game.placeLetter(1, 1, 'O');
        game.placeLetter(2, 2, 'S');

        assertTrue(game.checkDirection(1, 1, 1, 1));
    }
}
