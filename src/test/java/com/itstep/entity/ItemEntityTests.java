package com.itstep.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ItemEntityTests {

    @Test
    void setSplitDetailsTest() {
        Item item = Item.builder()
                .description("Сік апельсиновий")
                .price(50.0)
                .quantity(0.2)
                .totalPrice(50.0)
                .splitType("manual")
                .splitDetails(
                        new ArrayList<>(List.of(
                                SplitDetails.builder().userName("user2").value(20.0).build(),
                                SplitDetails.builder().userName("user1").value(30.0).build()
                        ))
                )
                .build();

        List<SplitDetails> splitDetails = List.of(SplitDetails.builder().userName("user1").value(12.0).build());

        item.setSplitDetails(splitDetails);

        assertThat(item.getSplitDetails().size()).isEqualTo(1);
        assertThat(item.getSplitDetails().getFirst().getItem()).isEqualTo(item);
    }

    @Test
    void setSplitDetailsWhenSplitDetailsIsNullTest() {
        Item item = Item.builder()
                .description("Сік апельсиновий")
                .price(50.0)
                .quantity(0.2)
                .totalPrice(50.0)
                .splitType("manual")
                .splitDetails(
                        new ArrayList<>(List.of(
                                SplitDetails.builder().userName("user2").value(20.0).build(),
                                SplitDetails.builder().userName("user1").value(30.0).build()
                        ))
                )
                .build();

        item.setSplitDetails(null);

        assertThat(item.getSplitDetails().size()).isEqualTo(0);
    }

    @Test
    void equalsTest() {
        Item o1 = Item.builder().id(1).price(20.0).build();
        Item o2 = Item.builder().id(1).price(10.0).build();
        assertTrue(o1.equals(o1));
        assertFalse(o1.equals(""));
        assertTrue(o1.equals(o2));
    }

    @Test
    void hashCodeTest() {
        Item o1 = Item.builder().id(1).price(20.0).build();
        assertEquals(o1.hashCode(), Integer.valueOf(1).hashCode());
        o1.setId(null);
        assertNotNull(o1.hashCode());
    }
}
