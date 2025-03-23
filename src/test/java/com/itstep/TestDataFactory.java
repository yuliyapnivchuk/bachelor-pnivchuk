package com.itstep;

import com.itstep.dto.*;
import com.itstep.entity.*;
import com.itstep.service.SplitType;

import java.util.*;

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
                .event(Event.builder().name("event name").build())
                .summary("Обід в ресторані")
                .totalAmount(300.0)
                .subtotalAmount(265.0)
                .currency("UAH")
                .splitType("byItem")
                .createdBy(User.builder().name("user1").email("user1@gmail.com").build())
                .payer(User.builder().name("user2").email("user2@gmail.com").build())
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

    public static List<ExpenseItemProjection> getExpenseItemProjection() {
        ExpenseItemProjection row1 = ExpenseItemProjection.builder()
                .expenseId(0)
                .itemId(1)
                .payer("user1")
                .totalPrice(550.0)
                .splitType(SplitType.MANUAL.type)
                .value(250.0)
                .userName("user2")
                .currency("UAH")
                .build();

        ExpenseItemProjection row2 = ExpenseItemProjection.builder()
                .expenseId(0)
                .itemId(2)
                .payer("user1")
                .totalPrice(550.0)
                .splitType(SplitType.MANUAL.type)
                .value(200.0)
                .userName("user3")
                .currency("UAH")
                .build();

        ExpenseItemProjection row3 = ExpenseItemProjection.builder()
                .expenseId(0)
                .itemId(3)
                .payer("user2")
                .totalPrice(1350.0)
                .splitType(SplitType.PERCENTAGE.type)
                .value(20.0)
                .userName("user3")
                .currency("UAH")
                .build();

        ExpenseItemProjection row4 = ExpenseItemProjection.builder()
                .expenseId(0)
                .itemId(4)
                .payer("user2")
                .totalPrice(1350.0)
                .splitType(SplitType.PERCENTAGE.type)
                .value(80.0)
                .userName("user4")
                .currency("UAH")
                .build();

        ExpenseItemProjection row5 = ExpenseItemProjection.builder()
                .expenseId(0)
                .itemId(5)
                .payer("user3")
                .totalPrice(50.0)
                .splitType(SplitType.SHARES.type)
                .value(2.0)
                .userName("user1")
                .currency("UAH")
                .build();

        ExpenseItemProjection row6 = ExpenseItemProjection.builder()
                .expenseId(0)
                .itemId(6)
                .payer("user3")
                .totalPrice(50.0)
                .splitType(SplitType.SHARES.type)
                .value(1.0)
                .userName("user2")
                .currency("UAH")
                .build();

        ExpenseItemProjection row7 = ExpenseItemProjection.builder()
                .expenseId(0)
                .itemId(7)
                .payer("user4")
                .totalPrice(250.0)
                .splitType(SplitType.EQUAL.type)
                .userName("user3")
                .currency("USD")
                .build();

        ExpenseItemProjection row8 = ExpenseItemProjection.builder()
                .expenseId(0)
                .itemId(8)
                .payer("user4")
                .totalPrice(250.0)
                .splitType(SplitType.EQUAL.type)
                .userName("user1")
                .currency("USD")
                .build();

        ExpenseItemProjection row9 = ExpenseItemProjection.builder()
                .expenseId(1)
                .payer("user1")
                .totalPrice(550.0)
                .splitType(SplitType.MANUAL.type)
                .value(250.0)
                .userName("user4")
                .currency("UAH")
                .build();

        ExpenseItemProjection row10 = ExpenseItemProjection.builder()
                .expenseId(2)
                .payer("user1")
                .totalPrice(550.0)
                .splitType(SplitType.MANUAL.type)
                .value(200.0)
                .userName("user2")
                .currency("UAH")
                .build();

        ExpenseItemProjection row11 = ExpenseItemProjection.builder()
                .expenseId(3)
                .payer("user2")
                .totalPrice(1350.0)
                .splitType(SplitType.PERCENTAGE.type)
                .value(20.0)
                .userName("user4")
                .currency("USD")
                .build();

        ExpenseItemProjection row12 = ExpenseItemProjection.builder()
                .expenseId(4)
                .payer("user2")
                .totalPrice(1350.0)
                .splitType(SplitType.PERCENTAGE.type)
                .value(80.0)
                .userName("user1")
                .currency("USD")
                .build();

        ExpenseItemProjection row13 = ExpenseItemProjection.builder()
                .expenseId(5)
                .payer("user2")
                .totalPrice(50.0)
                .splitType(SplitType.SHARES.type)
                .value(2.0)
                .userName("user1")
                .currency("UAH")
                .build();

        ExpenseItemProjection row14 = ExpenseItemProjection.builder()
                .expenseId(6)
                .payer("user2")
                .totalPrice(50.0)
                .splitType(SplitType.SHARES.type)
                .value(1.0)
                .userName("user2")
                .currency("UAH")
                .build();

        ExpenseItemProjection row15 = ExpenseItemProjection.builder()
                .expenseId(7)
                .payer("user3")
                .totalPrice(250.0)
                .splitType(SplitType.EQUAL.type)
                .userName("user2")
                .currency("EUR")
                .build();

        ExpenseItemProjection row16 = ExpenseItemProjection.builder()
                .expenseId(8)
                .payer("user3")
                .totalPrice(250.0)
                .splitType(SplitType.EQUAL.type)
                .userName("user1")
                .currency("EUR")
                .build();


        return List.of(
                row1,
                row2,
                row3,
                row4,
                row5,
                row6,
                row7,
                row8,
                row9,
                row10,
                row11,
                row12,
                row13,
                row14,
                row15,
                row16);
    }
}
