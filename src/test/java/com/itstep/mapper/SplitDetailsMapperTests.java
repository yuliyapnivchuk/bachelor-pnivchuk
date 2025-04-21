package com.itstep.mapper;

import com.itstep.dto.SplitDetailsDto;
import com.itstep.entity.Expense;
import com.itstep.entity.Item;
import com.itstep.entity.SplitDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.itstep.TestDataFactory.getExpense;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = MapperTestConfig.class)
public class SplitDetailsMapperTests {

    @Autowired
    SplitDetailsMapper splitDetailsMapper;

    @Test
    void toEntityTest() {
        Expense expense = getExpense();

        SplitDetailsDto splitDetailsDto = SplitDetailsDto.builder().userName("user1").value(10.0).build();

        Item item = Item.builder()
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

        SplitDetails splitDetails = splitDetailsMapper.toEntity(splitDetailsDto, expense, item);

        assertThat(splitDetails).isNotNull();
        assertThat(splitDetails.getExpense()).isEqualTo(expense);
        assertThat(splitDetails.getItem()).isEqualTo(item);
    }
}
