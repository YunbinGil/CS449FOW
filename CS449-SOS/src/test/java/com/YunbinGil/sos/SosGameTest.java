package com.YunbinGil.sos;

import main.java.com.YunbinGil.sos.SosGame;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SosGameTest {
    @Test
    void testPlaceLetter() {
        SosGame game = new SosGame(3);
        game.placeLetter(1, 1, 'S');
        assertEquals('S', game.getLetter(1, 1));  // 글자가 정상적으로 들어갔는지 확인
    }

    @Test
    void testCheckWin() {
        SosGame game = new SosGame(3);
        game.placeLetter(0, 0, 'S');
        game.placeLetter(0, 1, 'O');
        game.placeLetter(0, 2, 'S');
        assertTrue(game.checkWin());  // "SOS"가 완성되었는지 확인
    }

}
