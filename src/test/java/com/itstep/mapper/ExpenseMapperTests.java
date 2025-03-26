package com.itstep.mapper;

import com.itstep.dto.ExpenseDto;
import com.itstep.dto.ExpenseSubmissionDto;
import com.itstep.dto.ItemDto;
import com.itstep.dto.SplitDetailsDto;
import com.itstep.entity.*;
import com.itstep.repository.EventRepository;
import com.itstep.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.itstep.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = MapperTestConfig.class)
public class ExpenseMapperTests {

    @Autowired
    ExpenseMapper expenseMapper;

    @MockitoBean
    EventRepository eventRepository;

    @MockitoBean
    UserRepository userRepository;

    @Test
    void toEntityTest() {
        ExpenseDto expenseDto = getExpenseDto();
        expenseDto.setSplitDetails(List.of(SplitDetailsDto.builder().userName("user2").build()));

        when(eventRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Event(1, "name")));
        when(userRepository.findByName("user1")).thenReturn(Optional.of(new User(1, "user1", "user1@gmail.com")));
        when(userRepository.findByName("user2")).thenReturn(Optional.of(new User(2, "user2", "user2@gmail.com")));

        Expense actualExpense = expenseMapper.toEntity(expenseDto, eventRepository, userRepository);

        assertThat(actualExpense).isNotNull();
        assertThat(expenseDto.getCategory()).isEqualTo(actualExpense.getCategory());
        assertThat(expenseDto.getEventId()).isEqualTo(actualExpense.getEvent().getId());
        assertThat(expenseDto.getPayedBy()).isEqualTo(actualExpense.getPayer().getName());
        assertThat(expenseDto.getCreatedBy()).isEqualTo(actualExpense.getCreatedBy().getName());
        assertThat(expenseDto.getSummary()).isEqualTo(actualExpense.getSummary());
        assertThat(expenseDto.getTotalAmount()).isEqualTo(actualExpense.getTotalAmount());
        assertThat(expenseDto.getSubtotalAmount()).isEqualTo(actualExpense.getSubtotalAmount());
        assertThat(expenseDto.getCurrency()).isEqualTo(actualExpense.getCurrency());
        assertThat(expenseDto.getSplitType()).isEqualTo(actualExpense.getSplitType());
        assertThat(expenseDto.getTransactionDate()).isEqualTo(actualExpense.getTransactionDate());
        assertThat(expenseDto.getStatus()).isEqualTo(actualExpense.getStatus());
        assertThat(expenseDto.getImage()).isEqualTo(actualExpense.getImage());

        if (expenseDto.getItems() != null) {
            List<ItemDto> expectedItems = expenseDto.getItems();
            List<Item> actualItems = actualExpense.getItems();

            assertThat(expectedItems.getFirst().getPrice()).isEqualTo(actualItems.getFirst().getPrice());
            assertThat(expectedItems.getFirst().getDescription()).isEqualTo(actualItems.getFirst().getDescription());
            assertThat(expectedItems.getFirst().getQuantity()).isEqualTo(actualItems.getFirst().getQuantity());
            assertThat(expectedItems.getFirst().getTotalPrice()).isEqualTo(actualItems.getFirst().getTotalPrice());
            assertThat(expectedItems.getFirst().getSplitType()).isEqualTo(actualItems.getFirst().getSplitType());

            assertThat(expectedItems.get(1).getPrice()).isEqualTo(actualItems.get(1).getPrice());
            assertThat(expectedItems.get(1).getDescription()).isEqualTo(actualItems.get(1).getDescription());
            assertThat(expectedItems.get(1).getQuantity()).isEqualTo(actualItems.get(1).getQuantity());
            assertThat(expectedItems.get(1).getTotalPrice()).isEqualTo(actualItems.get(1).getTotalPrice());
            assertThat(expectedItems.get(1).getSplitType()).isEqualTo(actualItems.get(1).getSplitType());

            List<SplitDetailsDto> expectedSplitDetails = expenseDto.getItems().stream()
                    .flatMap(item -> item.getSplitDetails().stream())
                    .toList();

            List<SplitDetails> actualSplitDetails = actualExpense.getItems().stream()
                    .flatMap(item -> item.getSplitDetails().stream())
                    .toList();

            assertThat(expectedSplitDetails.getFirst().getUserName()).isEqualTo(actualSplitDetails.getFirst().getUserName());
            assertThat(expectedSplitDetails.getFirst().getValue()).isEqualTo(actualSplitDetails.getFirst().getValue());

            assertThat(expectedSplitDetails.get(1).getUserName()).isEqualTo(actualSplitDetails.get(1).getUserName());
            assertThat(expectedSplitDetails.get(1).getValue()).isEqualTo(actualSplitDetails.get(1).getValue());

            assertThat(expectedSplitDetails.getLast().getUserName()).isEqualTo(actualSplitDetails.getLast().getUserName());
            assertThat(expectedSplitDetails.getLast().getValue()).isEqualTo(actualSplitDetails.getLast().getValue());

        }
    }

    @Test
    void toEntityWhenSplitDetailsIsNullTest() {
        ExpenseDto expenseDto = getExpenseDto();
        expenseDto.setSplitDetails(null);
        expenseDto.getItems().forEach(item -> item.setSplitDetails(null));

        when(eventRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Event(1, "name")));
        when(userRepository.findByName("user1")).thenReturn(Optional.of(new User(1, "user1", "user1@gmail.com")));
        when(userRepository.findByName("user2")).thenReturn(Optional.of(new User(2, "user2", "user2@gmail.com")));

        Expense actualExpense = expenseMapper.toEntity(expenseDto, eventRepository, userRepository);

        assertThat(actualExpense).isNotNull();
        assertThat(expenseDto.getCategory()).isEqualTo(actualExpense.getCategory());
        assertThat(expenseDto.getEventId()).isEqualTo(actualExpense.getEvent().getId());
        assertThat(expenseDto.getPayedBy()).isEqualTo(actualExpense.getPayer().getName());
        assertThat(expenseDto.getCreatedBy()).isEqualTo(actualExpense.getCreatedBy().getName());
        assertThat(expenseDto.getSummary()).isEqualTo(actualExpense.getSummary());
        assertThat(expenseDto.getTotalAmount()).isEqualTo(actualExpense.getTotalAmount());
        assertThat(expenseDto.getSubtotalAmount()).isEqualTo(actualExpense.getSubtotalAmount());
        assertThat(expenseDto.getCurrency()).isEqualTo(actualExpense.getCurrency());
        assertThat(expenseDto.getSplitType()).isEqualTo(actualExpense.getSplitType());
        assertThat(expenseDto.getTransactionDate()).isEqualTo(actualExpense.getTransactionDate());
        assertThat(expenseDto.getStatus()).isEqualTo(actualExpense.getStatus());
        assertThat(expenseDto.getImage()).isEqualTo(actualExpense.getImage());

        if (expenseDto.getItems() != null) {
            List<ItemDto> expectedItems = expenseDto.getItems();
            List<Item> actualItems = actualExpense.getItems();

            assertThat(expectedItems.getFirst().getPrice()).isEqualTo(actualItems.getFirst().getPrice());
            assertThat(expectedItems.getFirst().getDescription()).isEqualTo(actualItems.getFirst().getDescription());
            assertThat(expectedItems.getFirst().getQuantity()).isEqualTo(actualItems.getFirst().getQuantity());
            assertThat(expectedItems.getFirst().getTotalPrice()).isEqualTo(actualItems.getFirst().getTotalPrice());
            assertThat(expectedItems.getFirst().getSplitType()).isEqualTo(actualItems.getFirst().getSplitType());

            assertThat(expectedItems.get(1).getPrice()).isEqualTo(actualItems.get(1).getPrice());
            assertThat(expectedItems.get(1).getDescription()).isEqualTo(actualItems.get(1).getDescription());
            assertThat(expectedItems.get(1).getQuantity()).isEqualTo(actualItems.get(1).getQuantity());
            assertThat(expectedItems.get(1).getTotalPrice()).isEqualTo(actualItems.get(1).getTotalPrice());
            assertThat(expectedItems.get(1).getSplitType()).isEqualTo(actualItems.get(1).getSplitType());

            for (Item tmpItem : actualExpense.getItems()){
                assertTrue(tmpItem.getSplitDetails().isEmpty());
            }
        }
    }

    @Test
    void toEntityWhenItemsIsNullTest() {
        ExpenseDto expenseDto = getExpenseDto();
        expenseDto.setItems(null);

        when(eventRepository.findById(any(Integer.class))).thenReturn(Optional.of(new Event(1, "name")));
        when(userRepository.findByName("user1")).thenReturn(Optional.of(new User(1, "user1", "user1@gmail.com")));
        when(userRepository.findByName("user2")).thenReturn(Optional.of(new User(2, "user2", "user2@gmail.com")));

        Expense actualExpense = expenseMapper.toEntity(expenseDto, eventRepository, userRepository);

        assertThat(actualExpense).isNotNull();
        assertThat(expenseDto.getCategory()).isEqualTo(actualExpense.getCategory());
        assertThat(expenseDto.getEventId()).isEqualTo(actualExpense.getEvent().getId());
        assertThat(expenseDto.getPayedBy()).isEqualTo(actualExpense.getPayer().getName());
        assertThat(expenseDto.getCreatedBy()).isEqualTo(actualExpense.getCreatedBy().getName());
        assertThat(expenseDto.getSummary()).isEqualTo(actualExpense.getSummary());
        assertThat(expenseDto.getTotalAmount()).isEqualTo(actualExpense.getTotalAmount());
        assertThat(expenseDto.getSubtotalAmount()).isEqualTo(actualExpense.getSubtotalAmount());
        assertThat(expenseDto.getCurrency()).isEqualTo(actualExpense.getCurrency());
        assertThat(expenseDto.getSplitType()).isEqualTo(actualExpense.getSplitType());
        assertThat(expenseDto.getTransactionDate()).isEqualTo(actualExpense.getTransactionDate());
        assertThat(expenseDto.getStatus()).isEqualTo(actualExpense.getStatus());
        assertThat(expenseDto.getImage()).isEqualTo(actualExpense.getImage());
        assertTrue(actualExpense.getItems().isEmpty());
    }

    @Test
    void toEntityWhenDtoIsNullTest() {
        Expense actualExpense = expenseMapper.toEntity(null, eventRepository, userRepository);
        assertThat(actualExpense).isNull();
    }

    @Test
    void toDtoTest() {
        Expense expectedExpense = getExpense();
        expectedExpense.setSplitDetails(List.of(SplitDetails.builder().userName("user2").build()));
        ExpenseDto actualExpense = expenseMapper.toDto(expectedExpense);

        assertThat(actualExpense).isNotNull();
        assertThat(expectedExpense.getId()).isEqualTo(actualExpense.getId());
        assertThat(expectedExpense.getCategory()).isEqualTo(actualExpense.getCategory());
        assertThat(expectedExpense.getEvent().getId()).isEqualTo(actualExpense.getEventId());
        assertThat(expectedExpense.getPayer().getName()).isEqualTo(actualExpense.getPayedBy());
        assertThat(expectedExpense.getCreatedBy().getName()).isEqualTo(actualExpense.getCreatedBy());
        assertThat(expectedExpense.getSummary()).isEqualTo(actualExpense.getSummary());
        assertThat(expectedExpense.getTotalAmount()).isEqualTo(actualExpense.getTotalAmount());
        assertThat(expectedExpense.getSubtotalAmount()).isEqualTo(actualExpense.getSubtotalAmount());
        assertThat(expectedExpense.getCurrency()).isEqualTo(actualExpense.getCurrency());
        assertThat(expectedExpense.getSplitType()).isEqualTo(actualExpense.getSplitType());
        assertThat(expectedExpense.getTransactionDate()).isEqualTo(actualExpense.getTransactionDate());
        assertThat(expectedExpense.getStatus()).isEqualTo(actualExpense.getStatus());
        assertThat(expectedExpense.getImage()).isEqualTo(actualExpense.getImage());

        if (expectedExpense.getItems() != null) {
            List<Item> expectedItems = expectedExpense.getItems();
            List<ItemDto> actualItems = actualExpense.getItems();

            assertThat(expectedItems.getFirst().getPrice()).isEqualTo(actualItems.getFirst().getPrice());
            assertThat(expectedItems.getFirst().getDescription()).isEqualTo(actualItems.getFirst().getDescription());
            assertThat(expectedItems.getFirst().getQuantity()).isEqualTo(actualItems.getFirst().getQuantity());
            assertThat(expectedItems.getFirst().getTotalPrice()).isEqualTo(actualItems.getFirst().getTotalPrice());
            assertThat(expectedItems.getFirst().getSplitType()).isEqualTo(actualItems.getFirst().getSplitType());

            assertThat(expectedItems.get(1).getPrice()).isEqualTo(actualItems.get(1).getPrice());
            assertThat(expectedItems.get(1).getDescription()).isEqualTo(actualItems.get(1).getDescription());
            assertThat(expectedItems.get(1).getQuantity()).isEqualTo(actualItems.get(1).getQuantity());
            assertThat(expectedItems.get(1).getTotalPrice()).isEqualTo(actualItems.get(1).getTotalPrice());
            assertThat(expectedItems.get(1).getSplitType()).isEqualTo(actualItems.get(1).getSplitType());

            List<SplitDetails> expectedSplitDetails = expectedExpense.getItems().stream()
                    .flatMap(item -> item.getSplitDetails().stream())
                    .toList();

            List<SplitDetailsDto> actualSplitDetails = actualExpense.getItems().stream()
                    .flatMap(item -> item.getSplitDetails().stream())
                    .toList();

            assertThat(expectedSplitDetails.getFirst().getUserName()).isEqualTo(actualSplitDetails.getFirst().getUserName());
            assertThat(expectedSplitDetails.getFirst().getValue()).isEqualTo(actualSplitDetails.getFirst().getValue());

            assertThat(expectedSplitDetails.get(1).getUserName()).isEqualTo(actualSplitDetails.get(1).getUserName());
            assertThat(expectedSplitDetails.get(1).getValue()).isEqualTo(actualSplitDetails.get(1).getValue());

            assertThat(expectedSplitDetails.getLast().getUserName()).isEqualTo(actualSplitDetails.getLast().getUserName());
            assertThat(expectedSplitDetails.getLast().getValue()).isEqualTo(actualSplitDetails.getLast().getValue());

        }
    }

    @Test
    void toDtoWhenSplitDetailsIsNullTest() {
        Expense expectedExpense = getExpense();
        expectedExpense.setSplitDetails(new ArrayList<>(List.of(SplitDetails.builder().userName("user2").build())));
        expectedExpense.setSplitDetails(null);
        expectedExpense.getItems().forEach(item -> item.setSplitDetails(null));
        ExpenseDto actualExpense = expenseMapper.toDto(expectedExpense);

        assertThat(actualExpense).isNotNull();
        assertThat(expectedExpense.getId()).isEqualTo(actualExpense.getId());
        assertThat(expectedExpense.getCategory()).isEqualTo(actualExpense.getCategory());
        assertThat(expectedExpense.getEvent().getId()).isEqualTo(actualExpense.getEventId());
        assertThat(expectedExpense.getPayer().getName()).isEqualTo(actualExpense.getPayedBy());
        assertThat(expectedExpense.getCreatedBy().getName()).isEqualTo(actualExpense.getCreatedBy());
        assertThat(expectedExpense.getSummary()).isEqualTo(actualExpense.getSummary());
        assertThat(expectedExpense.getTotalAmount()).isEqualTo(actualExpense.getTotalAmount());
        assertThat(expectedExpense.getSubtotalAmount()).isEqualTo(actualExpense.getSubtotalAmount());
        assertThat(expectedExpense.getCurrency()).isEqualTo(actualExpense.getCurrency());
        assertThat(expectedExpense.getSplitType()).isEqualTo(actualExpense.getSplitType());
        assertThat(expectedExpense.getTransactionDate()).isEqualTo(actualExpense.getTransactionDate());
        assertThat(expectedExpense.getStatus()).isEqualTo(actualExpense.getStatus());
        assertThat(expectedExpense.getImage()).isEqualTo(actualExpense.getImage());
        assertTrue(expectedExpense.getSplitDetails().isEmpty());

        if (expectedExpense.getItems() != null) {
            List<Item> expectedItems = expectedExpense.getItems();
            List<ItemDto> actualItems = actualExpense.getItems();

            assertThat(expectedItems.getFirst().getPrice()).isEqualTo(actualItems.getFirst().getPrice());
            assertThat(expectedItems.getFirst().getDescription()).isEqualTo(actualItems.getFirst().getDescription());
            assertThat(expectedItems.getFirst().getQuantity()).isEqualTo(actualItems.getFirst().getQuantity());
            assertThat(expectedItems.getFirst().getTotalPrice()).isEqualTo(actualItems.getFirst().getTotalPrice());
            assertThat(expectedItems.getFirst().getSplitType()).isEqualTo(actualItems.getFirst().getSplitType());

            assertThat(expectedItems.get(1).getPrice()).isEqualTo(actualItems.get(1).getPrice());
            assertThat(expectedItems.get(1).getDescription()).isEqualTo(actualItems.get(1).getDescription());
            assertThat(expectedItems.get(1).getQuantity()).isEqualTo(actualItems.get(1).getQuantity());
            assertThat(expectedItems.get(1).getTotalPrice()).isEqualTo(actualItems.get(1).getTotalPrice());
            assertThat(expectedItems.get(1).getSplitType()).isEqualTo(actualItems.get(1).getSplitType());

            for (ItemDto tmpItem : actualExpense.getItems()){
                assertTrue(tmpItem.getSplitDetails().isEmpty());
            }
        }
    }

    @Test
    void toDtoWhenItemsIsNullTest() {
        Expense expectedExpense = getExpense();
        expectedExpense.setItems(null);
        ExpenseDto actualExpense = expenseMapper.toDto(expectedExpense);

        assertThat(actualExpense).isNotNull();
        assertThat(expectedExpense.getId()).isEqualTo(actualExpense.getId());
        assertThat(expectedExpense.getCategory()).isEqualTo(actualExpense.getCategory());
        assertThat(expectedExpense.getEvent().getId()).isEqualTo(actualExpense.getEventId());
        assertThat(expectedExpense.getPayer().getName()).isEqualTo(actualExpense.getPayedBy());
        assertThat(expectedExpense.getCreatedBy().getName()).isEqualTo(actualExpense.getCreatedBy());
        assertThat(expectedExpense.getSummary()).isEqualTo(actualExpense.getSummary());
        assertThat(expectedExpense.getTotalAmount()).isEqualTo(actualExpense.getTotalAmount());
        assertThat(expectedExpense.getSubtotalAmount()).isEqualTo(actualExpense.getSubtotalAmount());
        assertThat(expectedExpense.getCurrency()).isEqualTo(actualExpense.getCurrency());
        assertThat(expectedExpense.getSplitType()).isEqualTo(actualExpense.getSplitType());
        assertThat(expectedExpense.getTransactionDate()).isEqualTo(actualExpense.getTransactionDate());
        assertThat(expectedExpense.getStatus()).isEqualTo(actualExpense.getStatus());
        assertThat(expectedExpense.getImage()).isEqualTo(actualExpense.getImage());
        assertTrue(expectedExpense.getItems().isEmpty());
    }

    @Test
    void toDtoWhenExpenseIsNullTest() {
        ExpenseDto actualExpense = expenseMapper.toDto((Expense) null);
        assertThat(actualExpense).isNull();
    }

    @Test
    void expenseDtoToExpenseSubmissionDtoTest() {
        ExpenseSubmissionDto expectedExpense = getExpenseSubmissionDto();
        expectedExpense.setSplitDetails(List.of(SplitDetailsDto.builder().userName("user2").build()));
        ExpenseDto actualExpense = expenseMapper.toDto(expectedExpense);

        assertThat(actualExpense).isNotNull();
        assertThat(expectedExpense.getCategory()).isEqualTo(actualExpense.getCategory());
        assertThat(expectedExpense.getEventId()).isEqualTo(actualExpense.getEventId());
        assertThat(expectedExpense.getPayedBy()).isEqualTo(actualExpense.getPayedBy());
        assertThat(expectedExpense.getCreatedBy()).isEqualTo(actualExpense.getCreatedBy());
        assertThat(expectedExpense.getSummary()).isEqualTo(actualExpense.getSummary());
        assertThat(expectedExpense.getTotalAmount()).isEqualTo(actualExpense.getTotalAmount());
        assertThat(expectedExpense.getSubtotalAmount()).isEqualTo(actualExpense.getSubtotalAmount());
        assertThat(expectedExpense.getCurrency()).isEqualTo(actualExpense.getCurrency());
        assertThat(expectedExpense.getSplitType()).isEqualTo(actualExpense.getSplitType());
        assertThat(expectedExpense.getTransactionDate()).isEqualTo(actualExpense.getTransactionDate());
        assertThat(expectedExpense.getStatus()).isEqualTo(actualExpense.getStatus());
        assertThat(expectedExpense.getImage()).isEqualTo(actualExpense.getImage());

        assertThat(expectedExpense.getItems()).containsExactlyInAnyOrderElementsOf(actualExpense.getItems());
    }

    @Test
    void expenseDtoToExpenseSubmissionDtoWhenSplitDetailsIsNullTest() {
        ExpenseSubmissionDto expectedExpense = getExpenseSubmissionDto();
        expectedExpense.setSplitDetails(null);
        expectedExpense.getItems().forEach(item -> item.setSplitDetails(null));
        ExpenseDto actualExpense = expenseMapper.toDto(expectedExpense);

        assertThat(actualExpense).isNotNull();
        assertThat(expectedExpense.getCategory()).isEqualTo(actualExpense.getCategory());
        assertThat(expectedExpense.getEventId()).isEqualTo(actualExpense.getEventId());
        assertThat(expectedExpense.getPayedBy()).isEqualTo(actualExpense.getPayedBy());
        assertThat(expectedExpense.getCreatedBy()).isEqualTo(actualExpense.getCreatedBy());
        assertThat(expectedExpense.getSummary()).isEqualTo(actualExpense.getSummary());
        assertThat(expectedExpense.getTotalAmount()).isEqualTo(actualExpense.getTotalAmount());
        assertThat(expectedExpense.getSubtotalAmount()).isEqualTo(actualExpense.getSubtotalAmount());
        assertThat(expectedExpense.getCurrency()).isEqualTo(actualExpense.getCurrency());
        assertThat(expectedExpense.getSplitType()).isEqualTo(actualExpense.getSplitType());
        assertThat(expectedExpense.getTransactionDate()).isEqualTo(actualExpense.getTransactionDate());
        assertThat(expectedExpense.getStatus()).isEqualTo(actualExpense.getStatus());
        assertThat(expectedExpense.getImage()).isEqualTo(actualExpense.getImage());
        assertThat(expectedExpense.getSplitDetails()).isNull();

        assertThat(expectedExpense.getItems()).containsExactlyInAnyOrderElementsOf(actualExpense.getItems());
    }

    @Test
    void expenseDtoToExpenseSubmissionDtoWhenItemsIsNullTest() {
        ExpenseSubmissionDto expectedExpense = getExpenseSubmissionDto();
        expectedExpense.setItems(null);
        ExpenseDto actualExpense = expenseMapper.toDto(expectedExpense);

        assertThat(actualExpense).isNotNull();
        assertThat(expectedExpense.getCategory()).isEqualTo(actualExpense.getCategory());
        assertThat(expectedExpense.getEventId()).isEqualTo(actualExpense.getEventId());
        assertThat(expectedExpense.getPayedBy()).isEqualTo(actualExpense.getPayedBy());
        assertThat(expectedExpense.getCreatedBy()).isEqualTo(actualExpense.getCreatedBy());
        assertThat(expectedExpense.getSummary()).isEqualTo(actualExpense.getSummary());
        assertThat(expectedExpense.getTotalAmount()).isEqualTo(actualExpense.getTotalAmount());
        assertThat(expectedExpense.getSubtotalAmount()).isEqualTo(actualExpense.getSubtotalAmount());
        assertThat(expectedExpense.getCurrency()).isEqualTo(actualExpense.getCurrency());
        assertThat(expectedExpense.getSplitType()).isEqualTo(actualExpense.getSplitType());
        assertThat(expectedExpense.getTransactionDate()).isEqualTo(actualExpense.getTransactionDate());
        assertThat(expectedExpense.getStatus()).isEqualTo(actualExpense.getStatus());
        assertThat(expectedExpense.getImage()).isEqualTo(actualExpense.getImage());

        assertThat(expectedExpense.getItems()).isNull();
    }

    @Test
    void expenseDtoToExpenseSubmissionDtoWhichIsNullTest() {
        ExpenseDto actualExpense = expenseMapper.toDto((ExpenseSubmissionDto) null);
        assertThat(actualExpense).isNull();
    }
}
