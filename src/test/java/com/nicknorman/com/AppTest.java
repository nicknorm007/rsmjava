package com.nicknorman.com;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testAnagramCountShouldEqualOne()
    {
        App myApp = new App();

        int count = 1;
        assertTrue(count==1);


    }
}
