package com.itstep.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.itstep.TestDataFactory.getExpense;
import static org.assertj.core.api.Assertions.assertThat;

public class ExpenseEntityTests {

    @Test
    void setSplitDetailsTest() {
        Expense expense = getExpense();

        List<SplitDetails> splitDetails = List.of(SplitDetails.builder().userName("user1").value(12.0).build());

        expense.setSplitDetails(splitDetails);

        assertThat(expense.getSplitDetails().size()).isEqualTo(1);
        assertThat(expense.getSplitDetails().getFirst().getExpense()).isEqualTo(expense);
    }

    @Test
    void setSplitDetailsWhenSplitDetailsIsNullTest() {
        Expense expense = getExpense();
        List<SplitDetails> splitDetails = new ArrayList<>();
        splitDetails.add(SplitDetails.builder().userName("user1").value(12.0).build());
        expense.setSplitDetails(splitDetails);

        expense.setSplitDetails(null);

        assertThat(expense.getSplitDetails().size()).isEqualTo(0);
    }

    @Test
    void setItemsTest() {
        Expense expense = getExpense();

        List<Item> items = List.of(Item.builder().description("test").price(1.2).build());

        expense.setItems(items);

        assertThat(expense.getItems().size()).isEqualTo(1);
        assertThat(expense.getItems().getFirst().getExpense()).isEqualTo(expense);
    }

    @Test
    void setItemsWhenItemsIsNullTest() {
        Expense expense = getExpense();

        expense.setItems(null);

        assertThat(expense.getItems().size()).isEqualTo(0);
    }
}
