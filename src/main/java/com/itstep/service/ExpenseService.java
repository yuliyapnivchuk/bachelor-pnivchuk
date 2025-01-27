package com.itstep.service;

import com.itstep.dto.ExpenseDto;
import com.itstep.dto.ExpenseSubmissionDto;
import com.itstep.dto.NoteDto;
import com.itstep.entity.*;
import com.itstep.exception.ExpenseNotFound;
import com.itstep.mapper.ExpenseMapper;
import com.itstep.mapper.NoteMapper;
import com.itstep.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.itstep.entity.ExpenseStatus.DRAFT;
import static com.itstep.entity.ExpenseStatus.SUBMITTED;
import static com.itstep.exception.ConstantsUtility.EXPENSE_WITH_SUCH_ID_NOT_FOUND;

@Service
@AllArgsConstructor
public class ExpenseService {
    private ExpenseRepository expenseRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private NoteRepository noteRepository;
    private SplitDetailsRepository splitDetailsRepository;
    private ExpenseMapper expenseMapper;
    private NoteMapper noteMapper;

    public ExpenseDto addExpense(ExpenseDto expenseDto) {
        Expense expense = expenseMapper.toEntity(expenseDto, eventRepository, userRepository);
        expense.setStatus(DRAFT.status);

        List<Item> items = expense.getItems();
        expense.setItems(null);

        List<SplitDetails> splitDetails = expense.getSplitDetails();
        expense.setSplitDetails(null);

        Expense savedExpense = expenseRepository.save(expense);

        if (items != null) {
            for (Item item : items) {
                item.setExpense(savedExpense);

                List<SplitDetails> splitItemDetails = item.getSplitDetails();
                item.setSplitDetails(null);

                Item savedItem = itemRepository.save(item);

                if (splitItemDetails != null) {
                    splitItemDetails.forEach(i -> i.setItem(savedItem));
                    List<SplitDetails> splitItemList = splitDetailsRepository.saveAll(splitItemDetails);
                    savedItem.setSplitDetails(splitItemList);
                }
            }
        }

        if (splitDetails != null) {
            splitDetails.forEach(item -> item.setExpense(savedExpense));
            splitDetailsRepository.saveAll(splitDetails);
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

    public ExpenseDto submitExpense(ExpenseSubmissionDto expenseSubmissionDto) {
        ExpenseDto expenseDto = expenseMapper.toDto(expenseSubmissionDto);
        expenseDto.setStatus(SUBMITTED.status);
        Expense expense = expenseMapper.toEntity(expenseDto, eventRepository, userRepository);
        Expense updatedExpense = updateExpense(expense);
        return expenseMapper.toDto(updatedExpense);
    }

    @Transactional
    public Expense updateExpense(Expense expense) {

        List<Item> expenseItems = expense.getItems();
        List<SplitDetails> expenseSplitDetails = expense.getSplitDetails();

        Expense existingExpense = expenseRepository.findById(expense.getId())
                .orElseThrow(() -> new ExpenseNotFound(EXPENSE_WITH_SUCH_ID_NOT_FOUND));
        List<Item> existingExpenseItems = itemRepository.findByExpense(existingExpense);
        List<SplitDetails> existingExpenseSplitDetails = splitDetailsRepository.findByExpense(existingExpense);
        existingExpense.setItems(null);
        existingExpense.setSplitDetails(null);

        existingExpense.setPayer(expense.getPayer());
        existingExpense.setSummary(expense.getSummary());
        existingExpense.setTotalAmount(expense.getTotalAmount());
        existingExpense.setSubtotalAmount(expense.getSubtotalAmount());
        existingExpense.setCurrency(expense.getCurrency());
        existingExpense.setSplitType(expense.getSplitType());
        existingExpense.setTransactionDate(expense.getTransactionDate());
        existingExpense.setTransactionTime(expense.getTransactionTime());
        existingExpense.setCategory(expense.getCategory());
        existingExpense.setStatus(expense.getStatus());

        Expense updatedExpense = expenseRepository.save(existingExpense);

        if (expenseSplitDetails != null) {
            expenseSplitDetails.forEach(item -> item.setExpense(expense));
            List<SplitDetails> updatedSplitDetails = updateSplitDetails(existingExpenseSplitDetails, expenseSplitDetails);
            updatedExpense.setSplitDetails(updatedSplitDetails);
        } else {
            List<SplitDetails> toBeDeleted = splitDetailsRepository.findByExpense(updatedExpense);
            toBeDeleted.forEach(i -> {
                i.setExpense(null);
                i.setItem(null);
                i.setUser(null);
            });
            splitDetailsRepository.deleteAll(toBeDeleted);
        }

        if (expenseItems != null) {
            expenseItems.forEach(item -> item.setExpense(updatedExpense));
            List<Item> updatedItems = updateItems(existingExpenseItems, expenseItems);
            updatedExpense.setItems(updatedItems);
        } else {
            List<Item> toBeDeleted = itemRepository.findByExpense(updatedExpense);
            itemRepository.deleteAll(toBeDeleted);
        }

        return updatedExpense;
    }

    private List<SplitDetails> updateSplitDetails(List<SplitDetails> existingSplitDetails, List<SplitDetails> splitDetails) {

        List<SplitDetails> updatedRecords = new ArrayList<>();

        for (SplitDetails record : splitDetails) {

            if (record.getId() != null) {
                Optional<SplitDetails> existingRecordOptional = splitDetailsRepository.findById(record.getId());

                if (existingRecordOptional.isPresent()) {
                    SplitDetails existingRecord = existingRecordOptional.get();
                    existingRecord.setUser(record.getUser());
                    existingRecord.setValue(record.getValue());
                    SplitDetails savedRecord = splitDetailsRepository.save(existingRecord);
                    updatedRecords.add(savedRecord);
                }
            } else {
                SplitDetails savedRecord = splitDetailsRepository.save(record);
                updatedRecords.add(savedRecord);
            }
        }

        List<SplitDetails> toBeDeleted = existingSplitDetails
                .stream()
                .filter(existing -> splitDetails.stream()
                        .filter(split -> split.getId() != null)
                        .noneMatch(split -> split.getId().equals(existing.getId()))
                ).toList();

        splitDetailsRepository.deleteAll(toBeDeleted);

        return updatedRecords;
    }

    private List<Item> updateItems(List<Item> existingExpenseItems, List<Item> expenseItems) {

        List<Item> updatedRecords = new ArrayList<>();

        for (Item record : expenseItems) {
            if (record.getId() != null) {
                Optional<Item> existingRecordOptional = itemRepository.findById(record.getId());

                if (existingRecordOptional.isPresent()) {
                    Item existingRecord = existingRecordOptional.get();
                    existingRecord.setDescription(record.getDescription());
                    existingRecord.setPrice(record.getPrice());
                    existingRecord.setQuantity(record.getQuantity());
                    existingRecord.setTotalPrice(record.getTotalPrice());
                    existingRecord.setSplitType(record.getSplitType());

                    List<SplitDetails> splitDetails = record.getSplitDetails();

                    if (splitDetails != null) {
                        splitDetails.forEach(item -> item.setItem(existingRecord));
                        List<SplitDetails> updatedSplitDetails =
                                updateSplitDetails(existingRecord.getSplitDetails(), splitDetails);
                        existingRecord.setSplitDetails(updatedSplitDetails);
                    } else {
                        List<SplitDetails> toBeDeleted = splitDetailsRepository.findByItem(existingRecord);
                        splitDetailsRepository.deleteAll(toBeDeleted);
                    }

                    record.getSplitDetails().forEach(i -> i.setItem(existingRecord));

                    List<SplitDetails> updatedSplitDetails =
                            updateSplitDetails(existingRecord.getSplitDetails(), record.getSplitDetails());

                    existingRecord.setSplitDetails(null);
                    Item updatedItem = itemRepository.save(existingRecord);
                    updatedItem.setSplitDetails(updatedSplitDetails);
                    updatedRecords.add(updatedItem);
                }
            } else {
                List<SplitDetails> splitDetails = record.getSplitDetails();
                record.setSplitDetails(null);
                Item savedItem = itemRepository.save(record);
                splitDetails.forEach(i -> i.setItem(savedItem));
                List<SplitDetails> savedRecords = splitDetailsRepository.saveAll(splitDetails);
                savedItem.setSplitDetails(savedRecords);
                updatedRecords.add(savedItem);
            }
        }

        if (existingExpenseItems != null) {
            List<Item> toBeDeleted = existingExpenseItems
                    .stream()
                    .filter(existing -> expenseItems.stream()
                            .filter(item -> item.getId() != null)
                            .noneMatch(item -> item.getId().equals(existing.getId()))
                    ).toList();

            itemRepository.deleteAll(toBeDeleted);
        }

        return updatedRecords;
    }
}