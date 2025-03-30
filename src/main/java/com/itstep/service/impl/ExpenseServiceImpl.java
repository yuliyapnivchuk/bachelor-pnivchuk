package com.itstep.service.impl;

import com.itstep.dto.ExpenseDto;
import com.itstep.entity.*;
import com.itstep.exception.ExpenseNotFound;
import com.itstep.mapper.ExpenseMapper;
import com.itstep.repository.*;
import com.itstep.service.ExpenseService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.itstep.entity.ExpenseStatus.DRAFT;
import static com.itstep.entity.ExpenseStatus.SUBMITTED;
import static com.itstep.exception.ConstantsUtility.EXPENSE_WITH_SUCH_ID_NOT_FOUND;

@Service
@AllArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private ExpenseRepository expenseRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private ExpenseMapper expenseMapper;

    @Transactional
    public ExpenseDto addExpense(ExpenseDto expenseDto) {
        Expense expense = expenseMapper.toEntity(expenseDto, eventRepository, userRepository);
        expense.setStatus(DRAFT.status);
        Expense savedExpense = expenseRepository.save(expense);
        return expenseMapper.toDto(savedExpense);
    }

    public ExpenseDto getExpense(Integer expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFound(EXPENSE_WITH_SUCH_ID_NOT_FOUND + expenseId));
        return expenseMapper.toDto(expense);
    }

    public List<ExpenseDto> getAllExpenses(Integer eventId) {
        List<Expense> expenseList = expenseRepository.findByEventId(eventId);
        return expenseList.stream().map(i -> expenseMapper.toDto(i)).toList();
    }

    public ExpenseDto updateExpense(ExpenseDto expenseDto) {
        Expense expense = expenseMapper.toEntity(expenseDto, eventRepository, userRepository);
        Expense savedExpense = expenseRepository.save(expense);
        return expenseMapper.toDto(savedExpense);
    }

    public ExpenseDto submitExpense(ExpenseDto expenseDto) {
        expenseDto.setStatus(SUBMITTED.status);
        return updateExpense(expenseDto);
    }

    public void delete(Integer id) {
        expenseRepository.deleteById(id);
    }
}