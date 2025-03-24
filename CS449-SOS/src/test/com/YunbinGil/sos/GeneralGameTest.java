package test.com.YunbinGil.sos;

import com.YunbinGil.sos.GeneralGame;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeneralGameTest {

    @Test
    public void testWinnerByCount() {
        GeneralGame game = new GeneralGame(3);
        game.placeLetter(0, 0, 'S');
        game.placeLetter(0, 1, 'O');
        game.placeLetter(0, 2, 'S');
        game.sosCountBlue++;

        game.placeLetter(1, 0, 'S');
        game.placeLetter(1, 1, 'O');
        game.placeLetter(1, 2, 'S');
        game.sosCountRed++;

        game.placeLetter(2, 0, 'S');
        game.placeLetter(2, 1, 'O');
        game.placeLetter(2, 2, 'S');
        game.sosCountBlue++;

        assertTrue(game.checkWinner());
        assertEquals("Blue Wins!", game.getWinner());
    }

    @Test
    public void testDraw() {
        GeneralGame game = new GeneralGame(3);
        game.sosCountBlue = 2;
        game.sosCountRed = 2;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                game.placeLetter(i, j, 'S');
            }
        }
        assertTrue(game.checkWinner());
        assertEquals("Draw!", game.getWinner());
    }
}
