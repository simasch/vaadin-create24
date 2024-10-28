package ch.martinelli.demo.jooq.data;

import ch.martinelli.demo.jooq.TestConfiguration;
import ch.martinelli.demo.jooq.data.dto.*;
import ch.martinelli.demo.jooq.db.tables.records.AthleteRecord;
import ch.martinelli.demo.jooq.db.tables.records.CompetitionRecord;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static ch.martinelli.demo.jooq.db.tables.Athlete.ATHLETE;
import static ch.martinelli.demo.jooq.db.tables.Club.CLUB;
import static ch.martinelli.demo.jooq.db.tables.Competition.COMPETITION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.*;

@Import(TestConfiguration.class)
@JooqTest
public class QueryTest {

    @Autowired
    private DSLContext dslContext;

    @Test
    void find_competitions() {
        Result<CompetitionRecord> competitions = dslContext
                .selectFrom(COMPETITION)
                .fetch();

        assertThat(competitions).hasSize(1);
    }

    @Test
    void insert_athlete() {
        Long id = dslContext
                .insertInto(ATHLETE)
                .columns(ATHLETE.FIRST_NAME, ATHLETE.LAST_NAME, ATHLETE.GENDER, ATHLETE.YEAR_OF_BIRTH,
                        ATHLETE.CLUB_ID, ATHLETE.ORGANIZATION_ID)
                .values("Mujinga", "Kambundji", "f", 1992,
                        1L, 1L)
                .returningResult(ATHLETE.ID)
                .fetchOneInto(Long.class);

        assertThat(id).isEqualTo(1);
    }

    @Test
    void insert_athlete_using_updatable_record() {
        AthleteRecord athlete = dslContext.newRecord(ATHLETE);
        athlete.setFirstName("Mujinga");
        athlete.setLastName("Kambundji");
        athlete.setGender("f");
        athlete.setYearOfBirth(1992);
        athlete.setClubId(1L);
        athlete.setOrganizationId(1L);

        athlete.store();

        assertThat(athlete.getId()).isNotNull();
    }

    @Test
    void projection() {
        Result<Record3<String, String, String>> athletes = dslContext
                .select(ATHLETE.FIRST_NAME, ATHLETE.LAST_NAME, CLUB.NAME)
                .from(ATHLETE)
                .join(CLUB).on(CLUB.ID.eq(ATHLETE.CLUB_ID))
                .fetch();

        assertThat(athletes).hasSize(1);
        assertThat(athletes.getFirst()).satisfies(athlete -> {
            assertThat(athlete.get(ATHLETE.FIRST_NAME)).isEqualTo("Armand");
            assertThat(athlete.get(ATHLETE.LAST_NAME)).isEqualTo("Duplantis");
            assertThat(athlete.get(CLUB.NAME)).isEqualTo("Louisiana State University");
        });
    }


    @Test
    void projection_using_java_record() {
        List<AthleteWithClubNameDTO> athletes = dslContext
                .select(ATHLETE.ID, ATHLETE.FIRST_NAME, ATHLETE.LAST_NAME, CLUB.NAME)
                .from(ATHLETE)
                .join(CLUB).on(CLUB.ID.eq(ATHLETE.CLUB_ID))
                .fetch(mapping(AthleteWithClubNameDTO::new));

        assertThat(athletes).hasSize(1);
        assertThat(athletes.getFirst()).satisfies(athlete -> {
            assertThat(athlete.firstName()).isEqualTo("Armand");
            assertThat(athlete.lastName()).isEqualTo("Duplantis");
            assertThat(athlete.clubName()).isEqualTo("Louisiana State University");
        });
    }

    @Test
    void implicit_join() {
        List<AthleteWithClubNameDTO> athletes = dslContext
                .select(ATHLETE.ID, ATHLETE.FIRST_NAME, ATHLETE.LAST_NAME, ATHLETE.club().NAME)
                .from(ATHLETE)
                .fetch(mapping(AthleteWithClubNameDTO::new));

        assertThat(athletes).hasSize(1);
        assertThat(athletes.getFirst()).satisfies(athlete -> {
            assertThat(athlete.firstName()).isEqualTo("Armand");
            assertThat(athlete.lastName()).isEqualTo("Duplantis");
            assertThat(athlete.clubName()).isEqualTo("Louisiana State University");
        });
    }

    @Test
    void functions() {
        List<AthleteWithClubNameDTO> athletes = dslContext
                .select(ATHLETE.ID, ATHLETE.FIRST_NAME, ATHLETE.LAST_NAME,
                        concat(ATHLETE.club().ABBREVIATION, val(" "), ATHLETE.club().NAME))
                .from(ATHLETE)
                .fetch(mapping(AthleteWithClubNameDTO::new));

        assertThat(athletes).hasSize(1);
        assertThat(athletes.getFirst()).satisfies(athlete -> {
            assertThat(athlete.firstName()).isEqualTo("Armand");
            assertThat(athlete.lastName()).isEqualTo("Duplantis");
            assertThat(athlete.clubName()).isEqualTo("LSU Louisiana State University");
        });
    }

    @Test
    void row_value_constructor() {
        List<AthleteWithClubDTO> athletes = dslContext
                .select(ATHLETE.ID, ATHLETE.FIRST_NAME, ATHLETE.LAST_NAME,
                        row(ATHLETE.club().ABBREVIATION, ATHLETE.club().NAME).mapping(ClubDTO::new))
                .from(ATHLETE)
                .fetch(mapping(AthleteWithClubDTO::new));

        assertThat(athletes).hasSize(1);
        assertThat(athletes.getFirst()).satisfies(athlete -> {
            assertThat(athlete.firstName()).isEqualTo("Armand");
            assertThat(athlete.lastName()).isEqualTo("Duplantis");
            assertThat(athlete.club().name()).isEqualTo("Louisiana State University");
        });
    }

    @Test
    void multiset_value_constructor() {
        List<ClubWithAthletesDTO> clubs = dslContext
                .select(CLUB.ABBREVIATION, CLUB.NAME,
                        multiset(
                                select(ATHLETE.ID, ATHLETE.FIRST_NAME, ATHLETE.LAST_NAME)
                                        .from(ATHLETE)
                                        .where(ATHLETE.CLUB_ID.eq(CLUB.ID))
                                        .orderBy(ATHLETE.FIRST_NAME, ATHLETE.LAST_NAME)
                        ).convertFrom(r -> r.map(mapping(AthleteDTO::new))))
                .from(CLUB)
                .orderBy(CLUB.ABBREVIATION)
                .fetch(mapping(ClubWithAthletesDTO::new));

        assertThat(clubs).hasSize(3);
        assertThat(clubs.getFirst()).satisfies(club -> {
            assertThat(club.abbreviation()).isEqualTo("LSU");
            assertThat(club.athletes()).hasSize(1);
        });
    }

    @Test
    void converter() {
        Gender.GenderConverter genderConverter = new Gender.GenderConverter();

        List<Gender> genders = dslContext
                .select(ATHLETE.GENDER.convert(genderConverter))
                .from(ATHLETE)
                .groupBy(ATHLETE.GENDER)
                .fetchInto(Gender.class);

        assertThat(genders).hasSize(1);
        assertThat(genders.getFirst()).isEqualTo(Gender.MALE);
    }
    @Test
    void delete() {
        int deletedRows = dslContext
                .deleteFrom(ATHLETE)
                .where(ATHLETE.ID.eq(1000L))
                .execute();

        assertThat(deletedRows).isEqualTo(1);
    }

    @Test
    void delete_using_updatable_record() {
        AthleteRecord athleteRecord = dslContext
                .selectFrom(ATHLETE)
                .where(ATHLETE.ID.eq(1000L))
                .fetchOne();

        assertThat(athleteRecord).isNotNull();

        int deletedRows = athleteRecord.delete();

        assertThat(deletedRows).isEqualTo(1);
    }
}
