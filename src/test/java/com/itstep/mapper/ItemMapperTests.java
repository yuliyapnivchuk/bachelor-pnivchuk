package com.itstep.mapper;

import com.itstep.dto.ItemDto;
import com.itstep.dto.SplitDetailsDto;
import com.itstep.entity.Expense;
import com.itstep.entity.Item;
import com.itstep.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static com.itstep.TestDataFactory.getExpense;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = MapperTestConfig.class)
public class ItemMapperTests {

    @Autowired
    ItemMapper itemMapper;

    @MockitoBean
    UserRepository userRepository;

    @Test
    void toEntityTest() {
        Expense expense = getExpense();

        ItemDto itemDto = ItemDto.builder()
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

        Item item = itemMapper.toEntity(itemDto, expense, userRepository);

        assertThat(item).isNotNull();
        assertThat(item.getExpense()).isEqualTo(expense);
    }
}
