package com.example.abc;

import org.junit.Test;
import com.example.abc.Logic;

import static org.junit.Assert.assertEquals;

public class Tests {

    @Test
    public void checkLogic(){
        Logic log = new Logic();

        assertEquals(null,log.canFit(30,70,7));
        assertEquals(-1, log.getWhoCell(34,-6));

    }
}
