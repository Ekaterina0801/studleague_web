package com.studleague.studleague.repository;

import com.studleague.studleague.entities.Flag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
@TestPropertySource("/application-test.properties")
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class FlagRepositoryTest {

    @Autowired
    private FlagRepository flagRepository;

    private Flag flag;

    @BeforeEach
    public void setup() {
        flag = new Flag();
        flag.setName("Test Flag");
    }

    @AfterEach
    void cleanUp() {
        flagRepository.deleteAll();
    }

    @Test
    public void testExistsByNameIgnoreCase() {
        flagRepository.save(flag);
        boolean exists = flagRepository.existsByNameIgnoreCase("test flag");
        assertThat(exists).isTrue();
    }

    @Test
    public void testFindByNameIgnoreCase() {
        flagRepository.save(flag);
        Optional<Flag> foundFlag = flagRepository.findByNameIgnoreCase("test flag");
        assertThat(foundFlag).isPresent();
        assertThat(foundFlag.get().getName()).isEqualTo("Test Flag");
    }

    @Test
    public void testFindByNameIgnoreCase_NotFound() {
        Optional<Flag> foundFlag = flagRepository.findByNameIgnoreCase("Non-existing Flag");
        assertThat(foundFlag).isNotPresent();
    }

}

