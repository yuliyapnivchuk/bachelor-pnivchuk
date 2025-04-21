package com.itstep.service;

import com.itstep.dto.BalanceDto;
import com.itstep.entity.Expense;
import com.itstep.entity.ExpenseItemProjection;
import com.itstep.exception.ExpenseNotFound;
import com.itstep.exception.NonExistingSplitType;
import com.itstep.mapper.MapperTestConfig;
import com.itstep.repository.ExpenseRepository;
import com.itstep.service.impl.BalanceServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static com.itstep.TestDataFactory.getExpense;
import static com.itstep.TestDataFactory.getExpenseItemProjection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = MapperTestConfig.class)
public class BalanceServiceTests {

    @Mock
    ExpenseRepository expenseRepository;

    @InjectMocks
    BalanceServiceImpl balanceService;

    @Test
    void getBalanceTest() {
        List<ExpenseItemProjection> expenseItemProjectionList = getExpenseItemProjection();

        when(expenseRepository.findUserIsOwedItems(anyString())).thenReturn(expenseItemProjectionList);
        when(expenseRepository.findUserOweItems(anyString())).thenReturn(expenseItemProjectionList);
        when(expenseRepository.findById(any())).thenReturn(Optional.ofNullable(getExpense()));

        BalanceDto result = balanceService.getBalance("user1");

        assertThat(result).isNotNull();
        assertThat(result.getTotalBalance().get("UAH")).isEqualTo(1627.5);
        assertThat(result.getTotalBalance().get("USD")).isEqualTo(-810.0);
        assertThat(result.getUserOwesTotal().get("EUR")).isEqualTo(250.0);
        assertThat(result.getUserOwesTotal().get("USD")).isEqualTo(1330.0);
        assertThat(result.getUserOwesTotal().get("UAH")).isEqualTo(638.75);
        assertThat(result.getUserIsOwedTotal().get("EUR")).isEqualTo(250.0);
        assertThat(result.getUserIsOwedTotal().get("USD")).isEqualTo(520.0);
        assertThat(result.getUserIsOwedTotal().get("UAH")).isEqualTo(2266.25);
        assertThat(result.getUserOwes().get("user2").get("USD")).isEqualTo(1080.0);
        assertThat(result.getUserOwes().get("user3").get("EUR")).isEqualTo(250.0);
        assertThat(result.getUserIsOwed().get("user2").get("EUR")).isEqualTo(250.0);
        assertThat(result.getUserIsOwed().get("user2").get("UAH")).isEqualTo(75.0);
        assertThat(result.getUserIsOwed().get("user3").get("USD")).isEqualTo(250.0);
        assertThat(result.getUserIsOwed().get("user3").get("UAH")).isEqualTo(428.75);
        assertThat(result.getUserIsOwed().get("user4").get("USD")).isEqualTo(20.0);
        assertThat(result.getUserIsOwed().get("user4").get("UAH")).isEqualTo(1123.75);
    }

    @Test
    void getBalanceWhenFeeAndTaxIsZeroTest() {
        List<ExpenseItemProjection> expenseItemProjectionList = getExpenseItemProjection();
        Expense expense = new Expense();
        expense.setTotalAmount(10.0);
        expense.setSubtotalAmount(10.0);

        when(expenseRepository.findUserIsOwedItems(anyString())).thenReturn(expenseItemProjectionList);
        when(expenseRepository.findUserOweItems(anyString())).thenReturn(expenseItemProjectionList);
        when(expenseRepository.findById(any())).thenReturn(Optional.of(expense));

        BalanceDto result = balanceService.getBalance("user1");

        assertThat(result).isNotNull();
    }

    @Test
    void getBalanceWhenExpenseNotFoundTest() {
        List<ExpenseItemProjection> expenseItemProjectionList = getExpenseItemProjection();

        when(expenseRepository.findUserIsOwedItems(anyString())).thenReturn(expenseItemProjectionList);
        when(expenseRepository.findUserOweItems(anyString())).thenReturn(expenseItemProjectionList);
        when(expenseRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(ExpenseNotFound.class, () -> balanceService.getBalance("user1"));
    }

    @Test
    void getBalanceNonExistingSplitTypeExceptionTest() {
        List<ExpenseItemProjection> expenseItemProjectionList = getExpenseItemProjection();
        expenseItemProjectionList.get(0).setSplitType("non existing split type");

        when(expenseRepository.findUserIsOwedItems(anyString())).thenReturn(expenseItemProjectionList);
        when(expenseRepository.findUserOweItems(anyString())).thenReturn(expenseItemProjectionList);
        when(expenseRepository.findById(any())).thenReturn(Optional.ofNullable(getExpense()));

        assertThrows(NonExistingSplitType.class, () -> balanceService.getBalance("user1"));
    }

    @Test
    void userIsOwedBalanceSplitFeeAndTaxExpenseNotFoundExceptionTest() {
        List<ExpenseItemProjection> expenseItemProjectionList = getExpenseItemProjection();

        when(expenseRepository.findUserIsOwedItems(anyString())).thenReturn(expenseItemProjectionList);
        when(expenseRepository.findUserOweItems(anyString())).thenReturn(expenseItemProjectionList);

        assertThrows(ExpenseNotFound.class, () -> balanceService.getBalance("user1"));
    }
}
