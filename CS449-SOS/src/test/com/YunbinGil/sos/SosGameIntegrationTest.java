package test.com.YunbinGil.sos;

import com.YunbinGil.sos.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SosGameIntegrationTest {

    @Test
    public void testComputerVsComputerFinishesGame() {
        SosGame game = new GeneralGame(3);
        SosGameController controller = new SosGameController(game);
        controller.setPlayerTypes(true, true);

        int turnLimit = 20;
        while (!controller.isGameOver() && turnLimit-- > 0) {
            controller.handleComputerTurn(game.isBlueTurn());
        }

        assertTrue(controller.isGameOver());
        assertNotNull(controller.getResultMessage());
    }
}