package ch.martinelli.demo.jooq.data.repository;

import ch.martinelli.demo.jooq.db.tables.records.ClubRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static ch.martinelli.demo.jooq.db.tables.Club.CLUB;

@Repository
public class ClubRepository {

    private final DSLContext dslContext;

    public ClubRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public Map<Long, ClubRecord> findAll() {
        return dslContext.selectFrom(CLUB)
                .orderBy(CLUB.NAME.asc())
                .fetchMap(ClubRecord::getId);
    }
}