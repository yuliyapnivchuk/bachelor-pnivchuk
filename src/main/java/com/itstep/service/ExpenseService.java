package com.itstep.service;

import com.itstep.dto.ExpenseDto;
import com.itstep.dto.NoteDto;
import com.itstep.entity.*;
import com.itstep.mapper.ExpenseMapper;
import com.itstep.mapper.NoteMapper;
import com.itstep.repository.*;
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
    private NoteRepository noteRepository;
    private SplitExpenseRepository splitExpenseRepository;
    private SplitItemRepository splitItemRepository;
    private ExpenseMapper expenseMapper;
    private NoteMapper noteMapper;

    public ExpenseDto addExpense(ExpenseDto expenseDto) {
        Expense expense = expenseMapper.toEntity(expenseDto, eventRepository, userRepository);
        expense.setStatus(DRAFT.status);
        List<Item> items = expense.getItems();
        expense.setItems(null);

        List<SplitExpense> splitDetails = expense.getSplitDetails();
        expense.setSplitDetails(null);

        Expense savedExpense = expenseRepository.save(expense);

        if (items != null) {
            for (Item item : items) {
                item.setExpense(savedExpense);

                List<SplitItem> splitItemDetails = item.getSplitDetails();
                item.setSplitDetails(null);

                Item savedItem = itemRepository.save(item);

                if (splitItemDetails != null) {
                    splitItemDetails.forEach(i -> i.setItem(savedItem));
                    List<SplitItem> splitItemList = splitItemRepository.saveAll(splitItemDetails);
                    savedItem.setSplitDetails(splitItemList);
                }
            }
        }

        if (splitDetails != null) {
            splitDetails.forEach(item -> item.setExpense(savedExpense));
            splitExpenseRepository.saveAll(splitDetails);
        }

        savedExpense.setItems(items);
        savedExpense.setSplitDetails(splitDetails);
        return expenseMapper.toDto(savedExpense);
    }

    public NoteDto addNote(NoteDto noteDto) {
        Note note = noteMapper.toEntity(noteDto, expenseRepository, userRepository);
        Note savedNote = noteRepository.save(note);
        return noteMapper.toDto(savedNote);
    }
}