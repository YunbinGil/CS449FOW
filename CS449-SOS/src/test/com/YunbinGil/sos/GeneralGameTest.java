    package test.com.YunbinGil.sos;

    import com.YunbinGil.sos.GeneralGame;
    import com.YunbinGil.sos.SosGameController;
    import org.junit.jupiter.api.Test;
    import static org.junit.jupiter.api.Assertions.*;

    public class GeneralGameTest {

        @Test
        public void testWinnerByCount() {
            GeneralGame game = new GeneralGame(3);
            SosGameController controller = new SosGameController(game);

            // Blue: SOS → row 0 (S-O-S)
            controller.handleMove(0, 0, 'S', true);
            controller.handleMove(0, 1, 'O', true);
            controller.handleMove(0, 2, 'S', true);

            // Red: SOS → row 1 (S-O-S)
            controller.handleMove(1, 0, 'S', false);
            controller.handleMove(1, 1, 'O', false);
            controller.handleMove(1, 2, 'S', false);

            // Blue: SOS → diagonal (0,2 - 1,1 - 2,0)
            controller.handleMove(2, 0, 'S', true);
            controller.handleMove(2, 1, 'O', false); // Red
            controller.handleMove(2, 2, 'S', true);  // Blue

            assertTrue(controller.isGameOver());
            assertEquals("Blue Wins!", controller.getResultMessage());
        }


        @Test
        public void testDraw_3x3_ExactlyTwoSOSPerPlayer() {
            GeneralGame game = new GeneralGame(3);
            SosGameController controller = new SosGameController(game);

            // Blue 1st SOS: diagonal (0,0)-(1,1)-(2,2)
            controller.handleMove(0, 0, 'S', true);
            controller.handleMove(1, 1, 'O', true);
            controller.handleMove(2, 2, 'S', true);

            // Red 1st SOS: reverse diagonal (0,2)-(1,1)-(2,0)
            controller.handleMove(0, 2, 'S', false);
            controller.handleMove(2, 0, 'S', false); // Already placed, but for logic flow
            // Note: (1,1) already used, but shared O in the middle is valid

            // Blue 2nd SOS: row (0,1)-(1,1)-(2,1)
            controller.handleMove(0, 1, 'S', true);
            controller.handleMove(2, 1, 'S', true); // Already used O in (1,1)

            // Red 2nd SOS: row (1,0)-(1,1)-(1,2)
            controller.handleMove(1, 0, 'S', false);
            controller.handleMove(1, 2, 'S', false); // Already used O in (1,1)

            assertTrue(controller.isGameOver());
            assertEquals("Draw!", controller.getResultMessage());
        }

    }
