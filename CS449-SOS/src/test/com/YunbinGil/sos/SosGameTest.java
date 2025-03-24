package test.com.YunbinGil.sos;

import com.YunbinGil.sos.SimpleGame;
import com.YunbinGil.sos.SosGame;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SosGameTest {

    @Test
    public void testCheckDirection_HorizontalSOS() {
        SosGame game = new SimpleGame(3);
        game.placeLetter(1, 0, 'S');
        game.placeLetter(1, 1, 'O');
        game.placeLetter(1, 2, 'S');
        assertTrue(game.checkDirection(1, 1, 0, 1));
    }

    @Test
    public void testCheckDirection_DiagonalSOS() {
        SosGame game = new SimpleGame(3);
        game.placeLetter(0, 0, 'S');
        game.placeLetter(1, 1, 'O');
        game.placeLetter(2, 2, 'S');
        assertTrue(game.checkDirection(1, 1, 1, 1));
    }
}
