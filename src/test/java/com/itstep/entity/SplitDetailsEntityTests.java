package com.itstep.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SplitDetailsEntityTests {

    @Test
    void equalsTest() {
        SplitDetails o1 = SplitDetails.builder().id(1).userName("user1").value(20.0).build();
        SplitDetails o2 = SplitDetails.builder().id(1).userName("user2").value(10.0).build();
        assertTrue(o1.equals(o1));
        assertFalse(o1.equals(""));
        assertTrue(o1.equals(o2));
    }

    @Test
    void hashCodeTest() {
        SplitDetails o1 = SplitDetails.builder().id(1).userName("user2").value(20.0).build();
        assertEquals(o1.hashCode(), Integer.valueOf(1).hashCode());
        o1.setId(null);
        assertNotEquals(0, o1.hashCode());
    }
}
