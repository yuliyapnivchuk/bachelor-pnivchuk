package com.itstep.service;

import com.itstep.dto.ExpenseDto;
import com.itstep.entity.Expense;
import com.itstep.exception.ExpenseNotFound;
import com.itstep.mapper.ExpenseMapper;
import com.itstep.mapper.MapperTestConfig;
import com.itstep.repository.ExpenseRepository;
import com.itstep.service.impl.ExpenseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;
import java.util.Optional;

import static com.itstep.TestDataFactory.getExpense;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = MapperTestConfig.class)
public class ExpenseServiceTests {

    @Mock
    ExpenseRepository expenseRepository;

    @MockitoSpyBean
    ExpenseMapper expenseMapper;

    @InjectMocks
    ExpenseServiceImpl expenseService;

    @Test
    void addExpenseTest() {
        Expense expense = getExpense();

        when(expenseMapper.toEntity(any(), any(), any())).thenReturn(expense);
        when(expenseRepository.save(any())).thenReturn(expense);

        ExpenseDto actualExpense = expenseService.addExpense(new ExpenseDto());

        assertThat(actualExpense).isNotNull();
        assertThat(expenseMapper.toDto(expense)).isEqualTo(actualExpense);
        assertThat(actualExpense.getStatus()).isEqualTo("DRAFT");
        verify(expenseMapper, times(1)).toEntity(any(), any(), any());
        verify(expenseRepository, times(1)).save(any());
    }

    @Test
    void getExpenseTest() {
        Expense expense = getExpense();
        when(expenseRepository.findById(anyInt())).thenReturn(Optional.of(expense));

        ExpenseDto actualExpense = expenseService.getExpense(1);

        assertThat(actualExpense).isNotNull();
        assertThat(expenseMapper.toDto(expense)).isEqualTo(actualExpense);
        verify(expenseRepository, times(1)).findById(any());
    }

    @Test
    void getExpenseWithSuchIdNotFoundTest() {
        assertThrows(ExpenseNotFound.class, () -> expenseService.getExpense(1));
    }

    @Test
    void submitExpenseTest() {
        Expense expense = getExpense();

        when(expenseMapper.toEntity(any(), any(), any())).thenReturn(expense);
        when(expenseRepository.save(any())).thenReturn(expense);

        ExpenseDto actualExpense = expenseService.submitExpense(new ExpenseDto());

        assertThat(actualExpense).isNotNull();
        assertThat(expenseMapper.toDto(expense)).isEqualTo(actualExpense);
        verify(expenseMapper, times(1)).toEntity(any(), any(), any());
        verify(expenseRepository, times(1)).save(any());
    }

    @Test
    void getAllExpensesTest() {
        Expense expense1 = getExpense();
        Expense expense2 = getExpense();
        List<Expense> expenses = List.of(expense1, expense2);

        when(expenseRepository.findByEventId(any())).thenReturn(expenses);

        List<ExpenseDto> result = expenseService.getAllExpenses(1);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(expenses.size());
        assertThat(result.getFirst().getCurrency()).isEqualTo(expenses.getFirst().getCurrency());
        assertThat(result.getFirst().getSubtotalAmount()).isEqualTo(expenses.getFirst().getSubtotalAmount());
        assertThat(result.getFirst().getTotalAmount()).isEqualTo(expenses.getFirst().getTotalAmount());

        verify(expenseRepository, times(1)).findByEventId(any());
    }

    @Test
    void deleteTest() {
        doNothing().when(expenseRepository).deleteById(any());

        expenseService.delete(1);

        verify(expenseRepository, times(1)).deleteById(any());
    }
}
