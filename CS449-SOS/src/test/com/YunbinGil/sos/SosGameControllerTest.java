package test.com.YunbinGil.sos;

import com.YunbinGil.sos.*;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SosGameControllerTest {

    @Test
    public void testHandleComputerTurnAddsMoveAndLine() {
        SosGame game = new GeneralGame(3);
        SosGameController controller = new SosGameController(game);
        controller.setPlayerTypes(true, false); // Blue is computer

        ComputerPlayer.Move move = controller.handleComputerTurn(true);
        assertNotNull(move);

        char placed = game.getLetter(move.row, move.col);
        assertEquals(move.letter, placed);

        List<SosLine> lines = controller.getSosLines();
        assertNotNull(lines);
    }
}
