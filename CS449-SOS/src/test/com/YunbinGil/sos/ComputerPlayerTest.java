package test.com.YunbinGil.sos;

import com.YunbinGil.sos.ComputerPlayer;
import com.YunbinGil.sos.ComputerPlayer.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComputerPlayerTest {

    @Test
    public void testBlocksSO_Pattern() {
        char[][] board = {
                {'S', 'O', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
        };
        ComputerPlayer cp = new ComputerPlayer();
        Move move = cp.decideMove(board);
        assertNotNull(move);
        assertEquals(0, move.row);
        assertEquals(2, move.col);
        assertEquals('S', move.letter);
    }

    @Test
    public void testFallbackRandomMove() {
        char[][] board = {
                {'S', 'O', 'S'},
                {'O', 'S', 'O'},
                {' ', ' ', ' '}
        };
        ComputerPlayer cp = new ComputerPlayer();
        Move move = cp.decideMove(board);
        assertNotNull(move);
        assertTrue(board[move.row][move.col] == ' ');
        assertTrue(move.letter == 'S' || move.letter == 'O');
    }
}
