package com.itstep;

import com.itstep.dto.*;
import com.itstep.entity.*;
import com.itstep.service.SplitType;

import java.time.LocalTime;
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
                .transactionTime("15:03:00")
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
                        new ArrayList<>(
                                List.of(SplitDetails.builder().userName("user2").build())
                        )
                )
                .build();

        Item item2 = Item.builder()
                .description("Капучино")
                .price(85.0)
                .quantity(2.0)
                .totalPrice(170.0)
                .splitType("shares")
                .splitDetails(
                        new ArrayList<>(List.of(
                                SplitDetails.builder().userName("user2").value(2.0).build(),
                                SplitDetails.builder().userName("user1").value(1.0).build()
                        ))
                )
                .build();

        Item item3 = Item.builder()
                .description("Реберця")
                .price(210.0)
                .quantity(1.0)
                .totalPrice(210.0)
                .splitType("%")
                .splitDetails(
                        new ArrayList<>(List.of(
                                SplitDetails.builder().userName("user2").value(30.0).build(),
                                SplitDetails.builder().userName("user1").value(70.0).build()
                        ))
                )
                .build();

        Item item4 = Item.builder()
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

        return Expense.builder()
                .event(Event.builder().name("event name").build())
                .summary("Обід в ресторані")
                .totalAmount(300.0)
                .subtotalAmount(265.0)
                .currency("UAH")
                .splitType("byItem")
                .transactionTime(LocalTime.of(15, 3, 0))
                .createdBy(User.builder().name("user1").email("user1@gmail.com").build())
                .payer(User.builder().name("user2").email("user2@gmail.com").build())
                .items(new ArrayList<>(List.of(item1, item2, item3, item4)))
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
        ExpenseItemProjection row1 = new ExpenseItemProjection();
        row1.setExpenseId(0);
        row1.setItemId(1);
        row1.setPayer("user1");
        row1.setTotalPrice(550.0);
        row1.setSplitType(SplitType.MANUAL.type);
        row1.setValue(250.0);
        row1.setUserName("user2");
        row1.setCurrency("UAH");

        ExpenseItemProjection row2 = new ExpenseItemProjection();
        row2.setExpenseId(0);
        row2.setItemId(2);
        row2.setPayer("user1");
        row2.setTotalPrice(550.0);
        row2.setSplitType(SplitType.MANUAL.type);
        row2.setValue(200.0);
        row2.setUserName("user3");
        row2.setCurrency("UAH");

        ExpenseItemProjection row3 = new ExpenseItemProjection();
        row3.setExpenseId(0);
        row3.setItemId(3);
        row3.setPayer("user2");
        row3.setTotalPrice(1350.0);
        row3.setSplitType(SplitType.PERCENTAGE.type);
        row3.setValue(20.0);
        row3.setUserName("user3");
        row3.setCurrency("UAH");

        ExpenseItemProjection row4 = new ExpenseItemProjection();
        row4.setExpenseId(0);
        row4.setItemId(4);
        row4.setPayer("user2");
        row4.setTotalPrice(1350.0);
        row4.setSplitType(SplitType.PERCENTAGE.type);
        row4.setValue(80.0);
        row4.setUserName("user4");
        row4.setCurrency("UAH");

        ExpenseItemProjection row5 = new ExpenseItemProjection();
        row5.setExpenseId(0);
        row5.setItemId(5);
        row5.setPayer("user3");
        row5.setTotalPrice(50.0);
        row5.setSplitType(SplitType.SHARES.type);
        row5.setValue(2.0);
        row5.setUserName("user1");
        row5.setCurrency("UAH");

        ExpenseItemProjection row6 = new ExpenseItemProjection();
        row6.setExpenseId(0);
        row6.setItemId(6);
        row6.setPayer("user3");
        row6.setTotalPrice(50.0);
        row6.setSplitType(SplitType.SHARES.type);
        row6.setValue(1.0);
        row6.setUserName("user2");
        row6.setCurrency("UAH");

        ExpenseItemProjection row7 = new ExpenseItemProjection();
        row7.setExpenseId(0);
        row7.setItemId(7);
        row7.setPayer("user4");
        row7.setTotalPrice(250.0);
        row7.setSplitType(SplitType.EQUAL.type);
        row7.setUserName("user3");
        row7.setCurrency("USD");

        ExpenseItemProjection row8 = new ExpenseItemProjection();
        row8.setExpenseId(0);
        row8.setItemId(8);
        row8.setPayer("user4");
        row8.setTotalPrice(250.0);
        row8.setSplitType(SplitType.EQUAL.type);
        row8.setUserName("user1");
        row8.setCurrency("USD");

        ExpenseItemProjection row9 = new ExpenseItemProjection();
        row9.setExpenseId(1);
        row9.setPayer("user2");
        row9.setTotalPrice(550.0);
        row9.setSplitType(SplitType.MANUAL.type);
        row9.setValue(250.0);
        row9.setUserName("user1");
        row9.setCurrency("UAH");

        ExpenseItemProjection row10 = new ExpenseItemProjection();
        row10.setExpenseId(2);
        row10.setPayer("user1");
        row10.setTotalPrice(550.0);
        row10.setSplitType(SplitType.MANUAL.type);
        row10.setValue(200.0);
        row10.setUserName("user2");
        row10.setCurrency("UAH");

        ExpenseItemProjection row11 = new ExpenseItemProjection();
        row11.setExpenseId(3);
        row11.setPayer("user2");
        row11.setTotalPrice(1350.0);
        row11.setSplitType(SplitType.PERCENTAGE.type);
        row11.setValue(20.0);
        row11.setUserName("user4");
        row11.setCurrency("USD");

        ExpenseItemProjection row12 = new ExpenseItemProjection();
        row12.setExpenseId(4);
        row12.setPayer("user2");
        row12.setTotalPrice(1350.0);
        row12.setSplitType(SplitType.PERCENTAGE.type);
        row12.setValue(80.0);
        row12.setUserName("user1");
        row12.setCurrency("USD");

        ExpenseItemProjection row13 = new ExpenseItemProjection();
        row13.setExpenseId(5);
        row13.setPayer("user2");
        row13.setTotalPrice(50.0);
        row13.setSplitType(SplitType.SHARES.type);
        row13.setValue(2.0);
        row13.setUserName("user1");
        row13.setCurrency("UAH");

        ExpenseItemProjection row14 = new ExpenseItemProjection();
        row14.setExpenseId(6);
        row14.setPayer("user2");
        row14.setTotalPrice(50.0);
        row14.setSplitType(SplitType.SHARES.type);
        row14.setValue(1.0);
        row14.setUserName("user2");
        row14.setCurrency("UAH");

        ExpenseItemProjection row15 = new ExpenseItemProjection();
        row15.setExpenseId(7);
        row15.setPayer("user3");
        row15.setTotalPrice(250.0);
        row15.setSplitType(SplitType.EQUAL.type);
        row15.setUserName("user2");
        row15.setCurrency("EUR");

        ExpenseItemProjection row16 = new ExpenseItemProjection();
        row16.setExpenseId(8);
        row16.setPayer("user3");
        row16.setTotalPrice(250.0);
        row16.setSplitType(SplitType.EQUAL.type);
        row16.setUserName("user1");
        row16.setCurrency("EUR");

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
