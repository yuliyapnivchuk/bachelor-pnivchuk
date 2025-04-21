package com.itstep.mapper;

import com.itstep.dto.NoteDto;
import com.itstep.entity.Note;
import com.itstep.entity.User;
import com.itstep.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static com.itstep.TestDataFactory.getNote;
import static com.itstep.TestDataFactory.getNoteDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = MapperTestConfig.class)
public class NoteMapperTests {

    @Autowired
    NoteMapper noteMapper;

    @MockitoBean
    UserRepository userRepository;

    @Test
    void toEntityTest() {
        NoteDto expectedNote = getNoteDto();

        when(userRepository.findByName(any())).thenReturn(Optional.of(new User(1, "user1", "user1@gmail.com")));

        Note actualNote = noteMapper.toEntity(expectedNote, userRepository);

        assertThat(actualNote).isNotNull();
        assertThat(expectedNote.getId()).isEqualTo(actualNote.getId());
        assertThat(expectedNote.getNoteText()).isEqualTo(actualNote.getNoteText());
        assertThat(expectedNote.getCreatedBy()).isEqualTo(actualNote.getCreatedBy().getName());
        assertThat(expectedNote.getExpenseId()).isEqualTo(actualNote.getExpenseId());
    }

    @Test
    void entityToDtoTest() {
        Note expectedNote = getNote();

        NoteDto actualNote = noteMapper.toDto(expectedNote);

        assertThat(actualNote).isNotNull();
        assertThat(expectedNote.getId()).isEqualTo(actualNote.getId());
        assertThat(expectedNote.getNoteText()).isEqualTo(actualNote.getNoteText());
        assertThat(expectedNote.getCreatedBy().getName()).isEqualTo(actualNote.getCreatedBy());
        assertThat(expectedNote.getExpenseId()).isEqualTo(actualNote.getExpenseId());
    }
}
