package com.itstep.service;

import com.itstep.dto.ExpenseDto;
import com.itstep.entity.Expense;
import com.itstep.entity.Item;
import com.itstep.mapper.ExpenseMapper;
import com.itstep.repository.EventRepository;
import com.itstep.repository.ExpenseRepository;
import com.itstep.repository.ItemRepository;
import com.itstep.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.itstep.entity.ExpenseStatus.DRAFT;

@Service
@AllArgsConstructor
public class ExpenseService {
    private ExpenseRepository expenseRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private ExpenseMapper expenseMapper;

    public ExpenseDto addExpense(ExpenseDto expenseDto) {
        Expense expense = expenseMapper.mapExpenseDtoToExpenseEntity(expenseDto, eventRepository, userRepository);
        expense.setStatus(DRAFT.status);
        List<Item> items = expense.getItems();
        expense.setItems(null);
        Expense savedExpense = expenseRepository.save(expense);

        if (items != null) {
            items.forEach(item -> item.setExpense(savedExpense));
            itemRepository.saveAll(items);
        }

        savedExpense.setItems(items);
        return expenseMapper.mapExpenseEntityToExpenseDto(savedExpense);
    }
}