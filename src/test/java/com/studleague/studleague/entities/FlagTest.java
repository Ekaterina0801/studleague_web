package com.studleague.studleague.entities;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FlagTest {

    private Flag flag;

    @BeforeEach
    public void setUp() {
        flag = Flag.builder()
                .name("Test Flag")
                .teams(new ArrayList<>())
                .build();
    }

    @Test
    public void testAddTeamToFlag() {
        Team team = new Team();
        team.setId(1L);

        flag.addTeamToFlag(team);

        assertThat(flag.getTeams()).contains(team);
    }

    @Test
    public void testAddSameTeamToFlag() {
        Team team = new Team();
        team.setId(1L);

        flag.addTeamToFlag(team);
        flag.addTeamToFlag(team);

        assertThat(flag.getTeams()).hasSize(1);
    }

    @Test
    public void testEqualsAndHashCode() {
        Flag flag1 = Flag.builder().name("Unique Flag").build();
        Flag flag2 = Flag.builder().name("Unique Flag").build();
        Flag flag3 = Flag.builder().name("Different Flag").build();

        assertThat(flag1).isEqualTo(flag2);
        assertThat(flag1).isNotEqualTo(flag3);

        assertThat(flag1.hashCode()).isEqualTo(flag2.hashCode());
        assertThat(flag1.hashCode()).isNotEqualTo(flag3.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "Flag(id=0, name=Test Flag, teams=[])";
        assertThat(flag.toString()).contains("Test Flag");
    }
}
