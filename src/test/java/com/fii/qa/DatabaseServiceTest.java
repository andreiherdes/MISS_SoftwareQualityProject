package com.fii.qa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DatabaseServiceTest {

    @Before
    public void B() {

    }

    @After
    public void C() {

    }

    @Test
    public void CreateDatabaseTest() {
        String hello = "Hello World";

        assertEquals (hello, "Hello World");
    }

}
