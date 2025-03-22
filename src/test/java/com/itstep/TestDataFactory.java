package com.itstep;

import com.itstep.dto.*;
import com.itstep.entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDataFactory {
    public static BalanceDto getBalanceDto() {
        BalanceDto balanceDto = new BalanceDto();
        Map<String, Double> totalBalance = new HashMap<>();
        totalBalance.put("UAH", -298.0);
        Map<String, Double> userOwesTotal = new HashMap<>();
        userOwesTotal.put("UAH", 298.0);
        Map<String, Map<String, Double>> userOwes = new HashMap<>();
        userOwes.put("user3", userOwesTotal);
        balanceDto.setTotalBalance(totalBalance);
        balanceDto.setUserOwesTotal(userOwesTotal);
        balanceDto.setUserOwes(userOwes);
        return balanceDto;
    }

    public static ExpenseDto getExpenseDto() {

        ItemDto item1 = ItemDto.builder()
                .description("Американо з молоком")
                .price(65.0)
                .quantity(3.0)
                .totalPrice(195.0)
                .splitType("=")
                .splitDetails(
                        List.of(SplitDetailsDto.builder().userName("user2").build())
                )
                .build();

        ItemDto item2 = ItemDto.builder()
                .description("Капучино")
                .price(85.0)
                .quantity(2.0)
                .totalPrice(170.0)
                .splitType("shares")
                .splitDetails(
                        List.of(
                                SplitDetailsDto.builder().userName("user2").value(2.0).build(),
                                SplitDetailsDto.builder().userName("user1").value(1.0).build()
                        )
                )
                .build();

        ItemDto item3 = ItemDto.builder()
                .description("Реберця")
                .price(210.0)
                .quantity(1.0)
                .totalPrice(210.0)
                .splitType("%")
                .splitDetails(
                        List.of(
                                SplitDetailsDto.builder().userName("user2").value(30.0).build(),
                                SplitDetailsDto.builder().userName("user1").value(70.0).build()
                        )
                )
                .build();

        ItemDto item4 = ItemDto.builder()
                .description("Сік апельсиновий")
                .price(50.0)
                .quantity(0.2)
                .totalPrice(50.0)
                .splitType("manual")
                .splitDetails(
                        List.of(
                                SplitDetailsDto.builder().userName("user2").value(20.0).build(),
                                SplitDetailsDto.builder().userName("user1").value(30.0).build()
                        )
                )
                .build();

        return ExpenseDto.builder()
                .eventId(1)
                .summary("Обід в ресторані")
                .totalAmount(300.0)
                .subtotalAmount(265.0)
                .currency("UAH")
                .splitType("byItem")
                .createdBy("user1")
                .payedBy("user2")
                .items(new ArrayList<>(List.of(item1, item2, item3, item4)))
                .build();

    }

    public static ExpenseSubmissionDto getExpenseSubmissionDto() {

        ItemDto item1 = ItemDto.builder()
                .description("Американо з молоком")
                .price(65.0)
                .quantity(3.0)
                .totalPrice(195.0)
                .splitType("=")
                .splitDetails(
                        List.of(SplitDetailsDto.builder().userName("user2").build())
                )
                .build();

        ItemDto item2 = ItemDto.builder()
                .description("Капучино")
                .price(85.0)
                .quantity(2.0)
                .totalPrice(170.0)
                .splitType("shares")
                .splitDetails(
                        List.of(
                                SplitDetailsDto.builder().userName("user2").value(2.0).build(),
                                SplitDetailsDto.builder().userName("user1").value(1.0).build()
                        )
                )
                .build();

        ItemDto item3 = ItemDto.builder()
                .description("Реберця")
                .price(210.0)
                .quantity(1.0)
                .totalPrice(210.0)
                .splitType("%")
                .splitDetails(
                        List.of(
                                SplitDetailsDto.builder().userName("user2").value(30.0).build(),
                                SplitDetailsDto.builder().userName("user1").value(70.0).build()
                        )
                )
                .build();

        ItemDto item4 = ItemDto.builder()
                .description("Сік апельсиновий")
                .price(50.0)
                .quantity(0.2)
                .totalPrice(50.0)
                .splitType("manual")
                .splitDetails(
                        List.of(
                                SplitDetailsDto.builder().userName("user2").value(20.0).build(),
                                SplitDetailsDto.builder().userName("user1").value(30.0).build()
                        )
                )
                .build();

        return ExpenseSubmissionDto.builder()
                .eventId(1)
                .summary("Обід в ресторані")
                .totalAmount(300.0)
                .subtotalAmount(265.0)
                .currency("UAH")
                .splitType("byItem")
                .createdBy("user1")
                .payedBy("user2")
                .items(new ArrayList<>(List.of(item1, item2, item3, item4)))
                .build();

    }

    public static Expense getExpense() {

        Item item1 = Item.builder()
                .description("Американо з молоком")
                .price(65.0)
                .quantity(3.0)
                .totalPrice(195.0)
                .splitType("=")
                .splitDetails(
                        List.of(SplitDetails.builder().userName("user2").build())
                )
                .build();

        Item item2 = Item.builder()
                .description("Капучино")
                .price(85.0)
                .quantity(2.0)
                .totalPrice(170.0)
                .splitType("shares")
                .splitDetails(
                        List.of(
                                SplitDetails.builder().userName("user2").value(2.0).build(),
                                SplitDetails.builder().userName("user1").value(1.0).build()
                        )
                )
                .build();

        Item item3 = Item.builder()
                .description("Реберця")
                .price(210.0)
                .quantity(1.0)
                .totalPrice(210.0)
                .splitType("%")
                .splitDetails(
                        List.of(
                                SplitDetails.builder().userName("user2").value(30.0).build(),
                                SplitDetails.builder().userName("user1").value(70.0).build()
                        )
                )
                .build();

        Item item4 = Item.builder()
                .description("Сік апельсиновий")
                .price(50.0)
                .quantity(0.2)
                .totalPrice(50.0)
                .splitType("manual")
                .splitDetails(
                        List.of(
                                SplitDetails.builder().userName("user2").value(20.0).build(),
                                SplitDetails.builder().userName("user1").value(30.0).build()
                        )
                )
                .build();

        return Expense.builder()
                .event(new Event(1, "event name"))
                .summary("Обід в ресторані")
                .totalAmount(300.0)
                .subtotalAmount(265.0)
                .currency("UAH")
                .splitType("byItem")
                .createdBy(new User(1, "user1", "user1@gmail.com"))
                .payer(new User(2, "user2", "user2@gmail.com"))
                .items(List.of(item1, item2, item3, item4))
                .build();
    }

    public static NoteDto getNoteDto() {
        return NoteDto.builder()
                .expenseId(1)
                .createdBy("user1")
                .noteText("this is note text")
                .build();
    }

    public static Note getNote() {
        return Note.builder()
                .expenseId(1)
                .createdBy(new User(1, "user1", "user1@gmail.com"))
                .noteText("this is note text")
                .build();
    }
}
