package test.com.YunbinGil.sos;

import com.YunbinGil.sos.SimpleGame;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleGameTest {

    @Test
    public void testWinCondition() {
        SimpleGame game = new SimpleGame(3);
        game.placeLetter(1, 0, 'S');
        game.placeLetter(1, 1, 'O');
        game.placeLetter(1, 2, 'S');
        assertTrue(game.checkWinner());
        assertEquals("Blue Wins!", game.getWinner());
    }

    @Test
    public void testDrawCondition() {
        SimpleGame game = new SimpleGame(3);
        game.placeLetter(0, 0, 'O');
        game.placeLetter(0, 1, 'S');
        game.placeLetter(0, 2, 'O');
        game.placeLetter(1, 0, 'O');
        game.placeLetter(1, 1, 'O');
        game.placeLetter(1, 2, 'O');
        game.placeLetter(2, 0, 'O');
        game.placeLetter(2, 1, 'O');
        game.placeLetter(2, 2, 'S');

        assertTrue(game.checkWinner());
        assertEquals("Draw! No winner.", game.getWinner());
    }

}
