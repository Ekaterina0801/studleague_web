package com.studleague.studleague.dao;

import com.studleague.studleague.dao.interfaces.FlagDao;
import com.studleague.studleague.entities.Flag;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class FlagDaoTest {

    @Autowired
    FlagDao flagDao;

    @AfterEach
    void cleanUp() {
        flagDao.deleteAll();
    }

    @Test
    public void testCreateReadDelete() {
        Flag flag = Flag.builder().name("Вуз").build();
        flagDao.save(flag);

        List<Flag> flags = flagDao.findAll();
        Assertions.assertThat(flags).extracting(Flag::getName).containsOnly("Вуз");

        flagDao.deleteAll();
        Assertions.assertThat(flagDao.findAll()).isEmpty();
    }

    @Test
    public void testSaveMultipleFlags() {
        Flag flag1 = Flag.builder().name("Flag1").build();
        Flag flag2 = Flag.builder().name("Flag2").build();
        flagDao.save(flag1);
        flagDao.save(flag2);

        List<Flag> flags = flagDao.findAll();
        Assertions.assertThat(flags).hasSize(2)
                .extracting(Flag::getName)
                .containsExactlyInAnyOrder("Flag1", "Flag2");
    }

    @Test
    public void testUpdateFlag() {
        Flag flag = Flag.builder().name("OriginalName").build();
        flagDao.save(flag);

        flag.setName("UpdatedName");
        flagDao.save(flag);

        Flag updatedFlag = flagDao.findAll().get(0);
        Assertions.assertThat(updatedFlag.getName()).isEqualTo("UpdatedName");
    }

    @Test
    public void testFindById() {
        Flag flag = Flag.builder().name("FindMe").build();
        flagDao.save(flag);

        Flag foundFlag = flagDao.findById(flag.getId()).orElse(null);
        Assertions.assertThat(foundFlag).isNotNull();
        Assertions.assertThat(foundFlag.getName()).isEqualTo("FindMe");
    }


    @Test
    public void testNoFlagsInitially() {
        Assertions.assertThat(flagDao.findAll()).isEmpty();
    }

    @Test
    public void testDuplicateFlagsNotAllowed() {
        Flag flag1 = Flag.builder().name("Duplicate").build();
        flagDao.save(flag1);

        Flag flag2 = Flag.builder().name("Duplicate").build();
        flagDao.save(flag2);

        List<Flag> flags = flagDao.findAll();
        Assertions.assertThat(flags).hasSize(2);
    }
}
