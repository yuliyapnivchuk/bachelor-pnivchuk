package com.itstep.repository;

import com.itstep.entity.Note;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static com.itstep.TestDataFactory.getNote;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
public class NoteRepositoryTests {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    NoteRepository noteRepository;

    @Test
    void saveTest() {
        Note note = getNote();
        note.setId(null);
        note.getCreatedBy().setId(null);

        Note result = noteRepository.save(note);
        assertThat(testEntityManager.find(Note.class, result.getId())).isEqualTo(note);
    }
}
