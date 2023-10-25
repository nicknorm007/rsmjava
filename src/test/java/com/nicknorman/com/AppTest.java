package com.nicknorman.com;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{


    @Test
    public void testRsmFooBarBasic()
    {
        App myApp = new App();
        String actual = myApp.fooBarRsm("1,2,3,4,5,6,45");
        String expected = "1,2,foo,4,bar,foo,foobar";

        assertTrue(actual.equals(expected));

    }
    @Test
    public void testRsmFooBarCopyNumber()
    {
        App myApp = new App();
        String actual = myApp.fooBarRsm("1,2,3,1,2,3,1,1");
        String expected = "1,2,foo,1-copy,2-copy,foo-copy,1-copy,1-copy";

        assertTrue(actual.equals(expected));

    }
    @Test
    public void testRsmFooBarCopyFooBar()
    {
        App myApp = new App();
        String actual = myApp.fooBarRsm("1,1,3,3,5,5,45,45");
        String expected = "1,1-copy,foo,foo-copy,bar,bar-copy,foobar,foobar-copy";

        assertTrue(actual.equals(expected));

    }
}
